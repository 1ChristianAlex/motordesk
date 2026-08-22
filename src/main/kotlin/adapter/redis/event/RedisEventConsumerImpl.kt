package com.khrix.adapter.redis.event

import com.khrix.adapter.redis.connection.RedisConnection
import com.khrix.adapter.redis.event.handler.RedisConsumerHandler
import com.khrix.application.email.publisher.EventConsumer
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import io.lettuce.core.Consumer
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.StreamMessage
import io.lettuce.core.XGroupCreateArgs
import io.lettuce.core.XNackMode
import io.lettuce.core.XReadArgs
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.slf4j.LoggerFactory
import java.time.Duration

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class RedisEventConsumerImpl(
    private val redis: RedisConnection,
    private val consumerHandlers: List<RedisConsumerHandler<Int>>,
) : EventConsumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    private suspend fun createConsumerGroup() {
        runCatching {
            redis.commands.xgroupCreate(
                XReadArgs.StreamOffset.from(
                    RedisEventKeys.EVENT_TYPE.value,
                    "0",
                ),
                RedisEventKeys.EVENT_GROUP.value,
                XGroupCreateArgs.Builder.mkstream(),
            )
        }.getOrNull()
    }

    override suspend fun start() {
        createConsumerGroup()
        while (true) {
            val messages =
                redis.commands.xreadgroup(
                    Consumer.from(
                        RedisEventKeys.EVENT_GROUP.value,
                        "worker-1",
                    ),
                    XReadArgs.Builder
                        .block(Duration.ofSeconds(5)),
                    XReadArgs.StreamOffset.lastConsumed(
                        RedisEventKeys.EVENT_TYPE.value,
                    ),
                )

            messages.collect { stream ->
                logger.info("Collecting redis item " + stream.id)

                runCatching {
                    processRedisMessage(stream)
                }.fold(
                    onSuccess = {
                        redis.commands.xack(
                            RedisEventKeys.EVENT_TYPE.value,
                            RedisEventKeys.EVENT_GROUP.value,
                            stream.id,
                        )
                        logger.info("Removing redis item " + stream.id)
                    },
                    onFailure = {
                        if (it is SendEmailUseCaseError.Retry) {
                            redis.commands.xnack(
                                RedisEventKeys.EVENT_TYPE.value,
                                RedisEventKeys.EVENT_GROUP.value,
                                XNackMode.SILENT,
                                stream.id,
                            )
                            logger.info("Readding redis item " + stream.id)
                        }
                    },
                )
            }
        }
    }

    private suspend fun processRedisMessage(stream: StreamMessage<String, String>) {
        stream.body.forEach { message ->
            supervisorScope {
                val data =
                    RedisDataEventHandler.unwrapEvent<Int>(
                        message.value,
                    )
                consumerHandlers.forEach { handler ->
                    launch {
                        if (data.event == handler.eventKey) {
                            handler.handle(data)
                        }
                    }
                }
            }
        }
    }
}
