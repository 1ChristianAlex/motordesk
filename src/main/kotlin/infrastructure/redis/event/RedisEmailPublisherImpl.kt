package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.publisher.EventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.infrastructure.redis.connection.RedisConnection
import io.lettuce.core.ExperimentalLettuceCoroutinesApi

class RedisEmailPublisherImpl(
    private val redis: RedisConnection,
) : EventPublisher {
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    override suspend fun publish(
        eventKeys: EventKeys,
        data: Int,
    ) {
        redis.commands.xadd(
            EventKeys.EVENT_TYPE.value,
            RedisDataEventHandler.wrapEvent(eventKeys, data),
        )
    }
}
