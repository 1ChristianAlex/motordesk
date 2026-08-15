package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.publisher.EventConsumer
import com.khrix.domain.email.publisher.EventKeys
import com.khrix.infrastructure.redis.connection.RedisConnection
import com.khrix.infrastructure.redis.event.handler.HandleConsumerEvent
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
import java.time.Duration

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class RedisEventConsumerImpl(
    private val redis: RedisConnection,
    private val consumerHandlers: List<HandleConsumerEvent<Any>>,
) : EventConsumer {
    private suspend fun createConsumerGroup() {
        runCatching {
            redis.commands.xgroupCreate(
                XReadArgs.StreamOffset.from(
                    EventKeys.EVENT_TYPE.value,
                    "0",
                ),
                EventKeys.EVENT_GROUP.value,
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
                        EventKeys.EVENT_GROUP.value,
                        "worker-1",
                    ),
                    XReadArgs.Builder
                        .block(Duration.ofSeconds(5)),
                    XReadArgs.StreamOffset.lastConsumed(
                        EventKeys.EVENT_TYPE.value,
                    ),
                )

            messages.collect { stream ->
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
                        }.join()
                }

                redis.commands.xack(
                    EventKeys.EVENT_TYPE.value,
                    EventKeys.EVENT_GROUP.value,
                    stream.id,
                )
            }
        }
    }
}
