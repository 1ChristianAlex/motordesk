package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.publisher.EventConsumer
import com.khrix.domain.email.publisher.EventKeys
import com.khrix.infrastructure.redis.connection.RedisConnection
import io.lettuce.core.Consumer
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.XGroupCreateArgs
import io.lettuce.core.XReadArgs
import kotlinx.serialization.json.Json
import java.time.Duration

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class RedisEventConsumerImpl(
    private val redis: RedisConnection,
) : EventConsumer {

    private suspend fun createConsumerGroup() {
        runCatching {
            redis.commands.xgroupCreate(
                XReadArgs.StreamOffset.from(
                    EventKeys.EVENT_NAME,
                    "0"
                ),
                EventKeys.EVENT_GROUP,
                XGroupCreateArgs.Builder.mkstream()
            )
        }.getOrNull()
    }

    override suspend fun start() {
        createConsumerGroup()
        while (true) {
            val messages =
                redis.commands.xreadgroup(
                    Consumer.from(
                        EventKeys.EVENT_GROUP,
                        "worker-1"
                    ),
                    XReadArgs.Builder
                        .block(Duration.ofSeconds(5)),
                    XReadArgs.StreamOffset.lastConsumed(
                        EventKeys.EVENT_NAME,
                    )
                )

            messages.collect { stream ->
                stream.body.forEach { message ->
                    val event =
                        Json.decodeFromString<EmailQueueItem>(
                            message.value
                        )

                    println(
                        "Enviar e-mail para ${event.recipient}"
                    )

                    redis.commands.xack(
                        EventKeys.EVENT_NAME,
                        EventKeys.EVENT_GROUP,
                        message.key
                    )
                }
            }
        }
    }
}