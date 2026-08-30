package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.publisher.EventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.infrastructure.redis.connection.RedisConnection
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import kotlinx.serialization.json.Json

class RedisEmailPublisherImpl(
    private val redis: RedisConnection,
) : EventPublisher {
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    override suspend fun publish(event: EmailQueueItem) {
        redis.commands.xadd(
            EventKeys.EVENT_NAME,
            "payload",
            Json.encodeToString(event),
        )
    }
}
