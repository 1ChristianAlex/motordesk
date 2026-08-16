package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.infrastructure.redis.event.RedisDataEvent

abstract class HandleConsumerEvent<T> : RedisConsumerHandler {
    abstract suspend fun internalHandler(payload: T)

    abstract val eventKey: EmailEventKeys

    abstract fun unwrapEvent(payload: String): RedisDataEvent<T>

    override suspend fun handle(payload: String) {
        val event = unwrapEvent(payload)

        if (event.event != eventKey) {
            return
        }

        internalHandler(event.payload)
    }
}
