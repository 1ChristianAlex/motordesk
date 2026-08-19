package com.khrix.infrastructure.redis.event.handler

import com.khrix.application.email.publisher.EmailEventKeys
import com.khrix.infrastructure.redis.event.RedisDataEvent

interface RedisConsumerHandler<T> {
    suspend fun handle(event: RedisDataEvent<T>)

    val eventKey: EmailEventKeys
}
