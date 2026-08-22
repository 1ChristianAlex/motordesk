package com.khrix.adapter.redis.connection

import com.khrix.adapter.app.InfraConfig
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands

class RedisConnection(
    infraConfig: InfraConfig,
) {
    private val redisClient: RedisClient =
        try {
            RedisClient.create(infraConfig.redisConfig.connectionString)
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
