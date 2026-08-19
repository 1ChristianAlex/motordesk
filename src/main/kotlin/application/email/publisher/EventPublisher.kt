package com.khrix.application.email.publisher

interface EventPublisher {
    suspend fun publish(
        eventKeys: EmailEventKeys,
        data: Int,
    )
}
