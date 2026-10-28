package com.khrix.adapter.app

import io.ktor.http.URLProtocol

class InfraConfigEnvImpl : InfraConfig {
    private fun requireEnv(name: String): String =
        System.getenv(name)
            ?: throw Exception("Environment variable '$name' is required")

    override val mongoConfig: MongoConfig =
        MongoConfig(
            url = requireEnv("MONGO_URL"),
            username = requireEnv("MONGO_USER"),
            password = requireEnv("MONGO_PASSWORD"),
            database = requireEnv("MONGO_DATABASE"),
            options = System.getenv("MONGO_OPTIONS").orEmpty(),
        )

    override val redisConfig: RedisConfig =
        RedisConfig(
            host = requireEnv("REDIS_HOST"),
            password = requireEnv("REDIS_PASSWORD"),
            port = requireEnv("REDIS_PORT"),
            tls = parseBooleanSetting(System.getenv("REDIS_TLS"), "REDIS_TLS"),
        )
    override val exposedConfig: ExposedConfig =
        ExposedConfig(
            url = requireEnv("DATABASE_URL"),
            driver = requireEnv("DATABASE_DRIVER"),
            user = requireEnv("DATABASE_USER"),
            password = requireEnv("DATABASE_PASSWORD"),
        )
    override val awsEmailConfig: AwsEmailConfig by lazy {
        AwsEmailConfig(
            region =
                System.getenv("AWS_REGION")
                    ?: System.getenv("AWS_DEFAULT_REGION")
                    ?: throw IllegalStateException("AWS_REGION or AWS_DEFAULT_REGION is required for AWS email"),
            fromAddress =
                System.getenv("AWS_SES_FROM_ADDRESS")
                    ?: throw IllegalStateException("AWS_SES_FROM_ADDRESS is required for AWS email"),
        )
    }
    override val jwtConfig: JwtConfig by lazy {
        JwtConfig(
            issuer = requireEnv("JWT_ISSUER"),
            audience = requireEnv("JWT_AUDIENCE"),
            realm = requireEnv("JWT_REALM"),
            secret = requireEnv("JWT_SECRET"),
        )
    }
    override val serverConfig by lazy {
        ServerConfig(
            host = requireEnv("HTTP_HOST"),
            port = requireEnv("HTTP_PORT").toInt(),
            protocol =
                requireEnv("HTTP_PROTOCOL").let {
                    URLProtocol.createOrDefault(it)
                },
        )
    }
}
