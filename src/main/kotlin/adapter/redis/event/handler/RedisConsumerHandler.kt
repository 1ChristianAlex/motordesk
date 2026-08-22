package com.khrix.adapter.redis.event.handler

import com.khrix.adapter.redis.event.RedisDataEvent
import com.khrix.application.email.publisher.EmailEventKeys

interface RedisConsumerHandler<T> {
    suspend fun handle(event: RedisDataEvent<T>)

    val eventKey: EmailEventKeys
}
