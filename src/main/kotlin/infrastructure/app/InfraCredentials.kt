package com.khrix.infrastructure.app


data class MongoConfig(
    val url: String,
    val username: String,
    val password: String,
    val database: String
) {
    val connectionString: String
        get() =
            "mongodb://$username:$password@" +
                    "${url.removePrefix("mongodb://")}/" +
                    "$database?authSource=admin"
}

data class RedisConfig(
    val host: String,
    val username: String,
    val password: String,
    val port: String
) {
    val connectionString: String
        get() = "redis://${username}:${password}@${host}:${port}"
}

data class ExposedConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)

interface InfraCredentials {
    val mongoConfig: MongoConfig
    val redisConfig: RedisConfig
    val exposedConfig: ExposedConfig
}

