package com.khrix.adapter.redis.event

import com.khrix.adapter.redis.connection.RedisConnection
import com.khrix.application.email.publisher.EmailEventKeys
import com.khrix.application.email.publisher.EventPublisher
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.slf4j.LoggerFactory

class RedisEmailPublisherImpl(
    private val redis: RedisConnection,
) : EventPublisher {
    private val logger = LoggerFactory.getLogger(javaClass)

    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    override suspend fun publish(
        eventKeys: EmailEventKeys,
        data: Int,
    ) {
        redis.commands.xadd(
            RedisEventKeys.EVENT_TYPE.value,
            "package",
            RedisDataEventHandler.wrapEvent(eventKeys, data),
        )
        logger.info("Adding item to redis")
    }
}
