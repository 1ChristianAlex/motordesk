package com.khrix.infrastructure.redis.event.handler

interface RedisConsumerHandler {
    suspend fun handle(payload: String)
}
