package com.khrix.infrastructure.redis.connection

import com.khrix.infrastructure.app.loadProperties
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import java.util.*


data class RedisConfig(
    val host: String,
    val username: String,
    val password: String,
    val port: String
) {
    val connectionString: String
        get() = "redis://${username}:${password}@${host}:${port}"
}

class RedisConnection {
    private val properties: Properties by lazy {
        loadProperties()
    }
    private val mongoConfig = RedisConfig(
        properties.getProperty("regis.host"),
        properties.getProperty("redis.user"),
        properties.getProperty("redis.password"),
        properties.getProperty("redis.port"),
    )

    private val redisClient: RedisClient = RedisClient.create(mongoConfig.connectionString)
    private val connection = redisClient.connect()

    // Expose the commands interface
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    val commands = connection.coroutines()

    fun close() {
        connection.close()
        redisClient.shutdown()
    }
}