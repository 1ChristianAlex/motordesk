package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EventKeys
import com.khrix.infrastructure.redis.event.RedisDataEvent

sealed class HandleConsumerEventError(
    override val message: String?,
) : Exception(message) {
    class NotFound : HandleConsumerEventError("Resource not available to create email template")

    class Retry : HandleConsumerEventError("Email sending failed, retrying...")

    class NoMoreRetriesAvailable : HandleConsumerEventError("Email sending failed, no more retries available")
}

abstract class HandleConsumerEvent<T> {
    abstract suspend fun internalHandler(payload: T)

    abstract val eventKey: EventKeys

    abstract fun unwrapEvent(payload: String): RedisDataEvent<T>

    suspend fun handle(payload: String) {
        val event = unwrapEvent(payload)

        if (event.event != eventKey) {
            return
        }

        internalHandler(event.payload)
    }
}
