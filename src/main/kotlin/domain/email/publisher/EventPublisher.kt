package com.khrix.domain.email.publisher

import com.khrix.domain.email.model.EmailQueueItem

interface EventPublisher {
    suspend fun publish(event: EmailQueueItem)

    companion object
}
