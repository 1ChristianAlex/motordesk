package com.khrix.infrastructure.redis.event

import com.khrix.domain.email.publisher.EventKeys
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RedisDataEvent<T>(
    val event: EventKeys,
    val payload: T,
)

class RedisDataEventHandler {
    companion object {
        inline fun <reified T> wrapEvent(
            event: EventKeys,
            data: T,
        ) = Json.encodeToString(RedisDataEvent(event, data))

        inline fun <reified T> unwrapEvent(data: String): RedisDataEvent<T> {
            val redisDataEvent = Json.decodeFromString<RedisDataEvent<T>>(data)
            return redisDataEvent
        }
    }
}
