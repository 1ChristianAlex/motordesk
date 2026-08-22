package com.khrix.adapter.redis

import com.khrix.adapter.redis.bootstrap.EventConsumerBootstrap
import com.khrix.adapter.redis.connection.RedisConnection
import com.khrix.adapter.redis.event.RedisEmailPublisherImpl
import com.khrix.adapter.redis.event.RedisEventConsumerImpl
import com.khrix.adapter.redis.event.handler.HandleEmailApprovalEvent
import com.khrix.adapter.redis.event.handler.HandleEmailUpdateEvent
import com.khrix.adapter.redis.event.handler.RedisConsumerHandler
import com.khrix.application.email.publisher.EventConsumer
import com.khrix.application.email.publisher.EventPublisher
import io.ktor.events.Events
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.DependencyRegistry

fun installRedisDI(
    dependencies: DependencyRegistry,
    monitor: Events,
) {
    with(dependencies) {
        provide(RedisConnection::class)

        provide<List<RedisConsumerHandler<Int>>> {
            listOf(
                HandleEmailUpdateEvent(resolve()),
                HandleEmailApprovalEvent(resolve()),
            )
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
