package com.khrix.infrastructure.redis.connection

import com.khrix.infrastructure.app.InfraCredentials
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands

class RedisConnection(
    infraCredentials: InfraCredentials,
) {
    private val redisClient: RedisClient =
        try {
            RedisClient.create(infraCredentials.redisConfig.connectionString)
        } catch (e: Exception) {
            throw RuntimeException("Failed to create Redis client: ${e.message}", e)
        }

    private val connection =
        try {
            redisClient.connect()
        } catch (e: Exception) {
            throw RuntimeException("Failed to connect to Redis: ${e.message}", e)
        }

    // Expose the commands interface
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    val commands: RedisCoroutinesCommands<String, String> = connection.coroutines()

    fun close() {
        connection.close()
        redisClient.shutdown()
    }
}
