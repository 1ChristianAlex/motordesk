package com.khrix.adapter.app

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
            options = properties.getProperty("mongo.options").orEmpty(),
        )
    }
    override val redisConfig by lazy {
        RedisConfig(
            host = properties.getProperty("redis.host"),
            password = properties.getProperty("redis.password"),
            port = properties.getProperty("redis.port"),
            tls = parseBooleanSetting(properties.getProperty("redis.tls"), "redis.tls"),
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
    override val awsEmailConfig: AwsEmailConfig by lazy {
        AwsEmailConfig(
            region =
                properties.getProperty("aws.region")
                    ?: throw IllegalStateException("aws.region is required for AWS SES"),
            fromAddress =
                properties.getProperty("aws.ses.from-address")
                    ?: throw IllegalStateException("aws.ses.from-address is required for AWS SES"),
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
