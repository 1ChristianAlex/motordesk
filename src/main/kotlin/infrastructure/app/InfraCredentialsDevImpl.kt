package com.khrix.infrastructure.app

import java.util.*

class InfraCredentialsDevImpl : InfraCredentials {
    private val properties: Properties by lazy {
        loadProperties()
    }

    override val mongoConfig: MongoConfig
        get() = MongoConfig(
            url = properties.getProperty("mongo.url"),
            username = properties.getProperty("mongo.user"),
            password = properties.getProperty("mongo.password"),
            database = properties.getProperty("mongo.database"),
        )

    override val redisConfig: RedisConfig
        get() = RedisConfig(
            host = properties.getProperty("redis.host"),
            username = properties.getProperty("redis.user"),
            password = properties.getProperty("redis.password"),
            port = properties.getProperty("redis.port"),
        )
    override val exposedConfig: ExposedConfig
        get() = ExposedConfig(
            url = properties.getProperty("db.url"),
            driver = properties.getProperty("db.driver"),
            user = properties.getProperty("db.user"),
            password = properties.getProperty("db.password")
        )
}