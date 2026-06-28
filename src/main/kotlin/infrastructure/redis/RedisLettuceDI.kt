package com.khrix.infrastructure.redis

import com.khrix.domain.email.publisher.EventConsumer
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.infrastructure.redis.bootstrap.EventConsumerBootstrap
import com.khrix.infrastructure.redis.connection.RedisConnection
import com.khrix.infrastructure.redis.event.RedisEmailPublisherImpl
import com.khrix.infrastructure.redis.event.RedisEventConsumerImpl
import io.ktor.events.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun redisLettuceDi(dependencies: DependencyRegistry, monitor: Events) {
    with(dependencies) {
        provide(RedisConnection::class)
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
