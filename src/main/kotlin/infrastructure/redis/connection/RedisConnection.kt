package com.khrix.infrastructure.redis.connection

import com.khrix.infrastructure.app.InfraCredentials
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines


class RedisConnection(infraCredentials: InfraCredentials) {
    private val redisClient: RedisClient = RedisClient.create(infraCredentials.redisConfig.connectionString)
    private val connection = redisClient.connect()

    // Expose the commands interface
    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    val commands = connection.coroutines()

    fun close() {
        connection.close()
        redisClient.shutdown()
    }
}