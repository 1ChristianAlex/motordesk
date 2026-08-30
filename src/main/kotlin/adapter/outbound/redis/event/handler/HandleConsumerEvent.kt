package com.khrix.adapter.outbound.redis.event.handler

import com.khrix.adapter.outbound.redis.event.RedisDataEvent
import org.slf4j.LoggerFactory

abstract class HandleConsumerEvent<T> : RedisConsumerHandler<T> {
    private val logger = LoggerFactory.getLogger(javaClass)

    abstract suspend fun internalHandler(payload: T)

    override suspend fun handle(event: RedisDataEvent<T>) {
        if (event.event != eventKey) {
            return
        }
        logger.info("Executing redis handler $eventKey")
        internalHandler(event.payload)
    }
}
