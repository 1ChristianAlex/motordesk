package com.khrix.domain.email.publisher

interface EventPublisher {
    suspend fun publish(
        eventKeys: EventKeys,
        data: Int,
    )
}
