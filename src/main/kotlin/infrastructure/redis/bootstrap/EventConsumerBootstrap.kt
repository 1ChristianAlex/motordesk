package com.khrix.infrastructure.redis.bootstrap

import com.khrix.domain.email.publisher.EventConsumer
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EventConsumerBootstrap(
    private val eventConsumer: EventConsumer,
    @Named("infraScope") private val applicationScope: CoroutineScope,
) {
    fun initialize() {
        applicationScope.launch {
            eventConsumer.start()
        }
    }
}
