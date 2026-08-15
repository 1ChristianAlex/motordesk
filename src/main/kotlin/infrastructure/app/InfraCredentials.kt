package com.khrix.infrastructure.app

import com.auth0.jwt.algorithms.Algorithm

data class MongoConfig(
    val url: String,
    val username: String,
    val password: String,
    val database: String,
) {
    val connectionString: String
        get() =
            "mongodb://$username:$password@" +
                "${url.removePrefix("mongodb://")}/" +
                "$database?authSource=admin"
}

data class RedisConfig(
    val host: String,
    val password: String,
    val port: String,
) {
    val connectionString: String
        get() = "redis://$password@$host:$port"
}

data class ExposedConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
)

data class AzureConfig(
    val accessKey: String,
    val communicationEndpoint: String,
)

data class JwtConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    private val secret: String,
) {
    val algorithm = Algorithm.HMAC256(secret)
}

interface InfraCredentials {
    val mongoConfig: MongoConfig
    val redisConfig: RedisConfig
    val exposedConfig: ExposedConfig
    val azureConfig: AzureConfig
    val jwtConfig: JwtConfig
}
