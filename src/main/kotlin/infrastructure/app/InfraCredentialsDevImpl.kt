package com.khrix.infrastructure.app

import com.khrix.BuildKonfig
import java.util.Properties

class InfraCredentialsDevImpl : InfraCredentials {
    private val properties: Properties by lazy {
        Properties().apply {
            object {}.javaClass.classLoader.getResourceAsStream(BuildKonfig.PROPERTIES_FILE)?.use {
                load(it)
            }
        }
    }

    override val mongoConfig by lazy {
        MongoConfig(
            url = properties.getProperty("mongo.url"),
            username = properties.getProperty("mongo.user"),
            password = properties.getProperty("mongo.password"),
            database = properties.getProperty("mongo.database"),
        )
    }
    override val redisConfig by lazy {
        RedisConfig(
            host = properties.getProperty("redis.host"),
            password = properties.getProperty("redis.password"),
            port = properties.getProperty("redis.port"),
        )
    }
    override val exposedConfig by lazy {
        ExposedConfig(
            url = properties.getProperty("db.url"),
            driver = properties.getProperty("db.driver"),
            user = properties.getProperty("db.user"),
            password = properties.getProperty("db.password"),
        )
    }
    override val azureConfig by lazy {
        AzureConfig(
            accessKey = properties.getProperty("azure.accessKey"),
            communicationEndpoint = properties.getProperty("azure.communicationEndpoint"),
        )
    }
    override val jwtConfig: JwtConfig by lazy {
        JwtConfig(
            issuer = properties.getProperty("jwt.issuer"),
            audience = properties.getProperty("jwt.audience"),
            realm = properties.getProperty("jwt.realm"),
            secret = properties.getProperty("jwt.secret"),
        )
    }
}
