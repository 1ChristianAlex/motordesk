package com.khrix.infrastructure.redis.bootstrap

import com.khrix.domain.email.publisher.EventConsumer

class EventConsumerBootstrap(
    private val eventConsumer: EventConsumer
) {

    suspend fun initialize() {
        eventConsumer.start()
    }
}