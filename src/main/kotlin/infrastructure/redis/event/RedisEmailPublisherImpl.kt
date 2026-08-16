package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.infrastructure.redis.connection.RedisConnection
import io.lettuce.core.ExperimentalLettuceCoroutinesApi

class RedisEmailPublisherImpl(
    private val redis: RedisConnection,
) : EventPublisher {
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    override suspend fun publish(
        eventKeys: EmailEventKeys,
        data: Int,
    ) {
        redis.commands.xadd(
            RedisEventKeys.EVENT_TYPE.value,
            RedisDataEventHandler.wrapEvent(eventKeys, data),
        )
    }
}
