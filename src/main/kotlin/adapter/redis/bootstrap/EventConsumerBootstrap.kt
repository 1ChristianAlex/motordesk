package com.khrix.adapter.redis.bootstrap

import com.khrix.application.email.publisher.EventConsumer
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class EventConsumerBootstrap(
    private val eventConsumer: EventConsumer,
    @Named("infraScope") private val applicationScope: CoroutineScope,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun initialize() {
        applicationScope.launch {
            logger.info("Starting redis consumer application initialization")
            eventConsumer.start()
        }
    }
}
