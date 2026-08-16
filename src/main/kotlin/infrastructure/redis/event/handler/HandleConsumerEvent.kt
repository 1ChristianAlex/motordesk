package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.infrastructure.redis.event.RedisDataEvent
import org.slf4j.LoggerFactory

abstract class HandleConsumerEvent<T> : RedisConsumerHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    abstract suspend fun internalHandler(payload: T)

    abstract val eventKey: EmailEventKeys

    abstract fun unwrapEvent(payload: String): RedisDataEvent<T>

    override suspend fun handle(payload: String) {
        val event = unwrapEvent(payload)

        if (event.event != eventKey) {
            return
        }
        logger.info("Executing redis handler $eventKey")
        internalHandler(event.payload)
    }
}
