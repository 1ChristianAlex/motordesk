package com.khrix.infrastructure.redis.event

enum class RedisEventKeys(
    val value: String,
) {
    EVENT_TYPE("email"),
    EVENT_GROUP("email-workers"),
}
