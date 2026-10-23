package com.khrix.adapter.app

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InfraConfigTest {
    @Test
    fun `builds a MongoDB connection string with provider options`() {
        val config =
            MongoConfig(
                url = "documentdb.example:27017",
                username = "app",
                password = "password",
                database = "motordesk",
                options = "tls=true&retryWrites=false",
            )

        assertEquals(
            "mongodb://app:password@documentdb.example:27017/motordesk?authSource=admin&tls=true&retryWrites=false",
            config.connectionString,
        )
    }

    @Test
    fun `uses TLS Redis scheme when enabled`() {
        val config = RedisConfig(host = "redis.example", password = "token", port = "6379", tls = true)

        assertEquals("rediss://:token@redis.example:6379", config.connectionString)
    }

    @Test
    fun `rejects malformed boolean configuration`() {
        assertFailsWith<IllegalArgumentException> {
            parseBooleanSetting("yes", "REDIS_TLS")
        }
    }
}
