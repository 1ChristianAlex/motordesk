package com.khrix.infrastructure.redis

import com.khrix.domain.email.publisher.EventConsumer
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.infrastructure.redis.bootstrap.EventConsumerBootstrap
import com.khrix.infrastructure.redis.connection.RedisConnection
import com.khrix.infrastructure.redis.event.RedisEmailPublisherImpl
import com.khrix.infrastructure.redis.event.RedisEventConsumerImpl
import com.khrix.infrastructure.redis.event.handler.HandleEmailApprovalEvent
import com.khrix.infrastructure.redis.event.handler.RedisConsumerHandler
import io.ktor.events.Events
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.DependencyRegistry

fun redisLettuceDi(
    dependencies: DependencyRegistry,
    monitor: Events,
) {
    with(dependencies) {
        provide(RedisConnection::class)
        provide("consumerHandlerList") {
            listOf<RedisConsumerHandler>(HandleEmailApprovalEvent(resolve(), resolve()))
        }
        provide<EventPublisher>(RedisEmailPublisherImpl::class)
        provide<EventConsumer>(RedisEventConsumerImpl::class)

        provide(EventConsumerBootstrap::class)

        val eventConsumer: EventConsumerBootstrap by dependencies
        eventConsumer.initialize()

        monitor.subscribe(ApplicationStopping) {
            val redisConnection: RedisConnection by dependencies
            redisConnection.close()
        }
    }
}
