package com.khrix.infrastructure.app

import com.khrix.BuildKonfig
import io.ktor.http.URLProtocol
import java.util.Properties

class InfraConfigDevImpl : InfraConfig {
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
            accessKey = properties.getProperty("azure.communication.access-key"),
            communicationEndpoint = properties.getProperty("azure.communication.endpoint"),
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
    override val serverConfig by lazy {
        ServerConfig(
            host = properties.getProperty("http.host") ?: "127.0.0.1",
            port = properties.getProperty("http.port")?.toInt() ?: 8080,
            protocol =
                properties.getProperty("http.protocol")?.let {
                    URLProtocol.createOrDefault(it)
                } ?: URLProtocol.HTTP,
        )
    }
}
