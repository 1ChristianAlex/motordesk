package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.publisher.EventConsumer
import com.khrix.infrastructure.redis.connection.RedisConnection
import com.khrix.infrastructure.redis.event.handler.RedisConsumerHandler
import io.ktor.server.plugins.di.annotations.Named
import io.lettuce.core.Consumer
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.XGroupCreateArgs
import io.lettuce.core.XReadArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.time.Duration

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class RedisEventConsumerImpl(
    private val redis: RedisConnection,
    @Named("consumerHandlerList") private val consumerHandlers: List<RedisConsumerHandler>,
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
                stream.body.forEach { message ->
                    val myScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
                    myScope
                        .launch {
                            val asyncHandler =
                                consumerHandlers.map { handler ->
                                    async {
                                        handler.handle(message.value)
                                    }
                                }

                            asyncHandler.awaitAll()
                        }
                }

                redis.commands.xack(
                    RedisEventKeys.EVENT_TYPE.value,
                    RedisEventKeys.EVENT_GROUP.value,
                    stream.id,
                )
                logger.info("Removing redis item " + stream.id)
            }
        }
    }
}
