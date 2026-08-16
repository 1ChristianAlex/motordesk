package com.khrix.infrastructure.app

class InfraCredentialsEnvImpl : InfraCredentials {
    private fun requireEnv(name: String): String =
        System.getenv(name)
            ?: throw Exception("Environment variable '$name' is required")

    override val mongoConfig: MongoConfig =
        MongoConfig(
            url = requireEnv("MONGO_URL"),
            username = requireEnv("MONGO_USER"),
            password = requireEnv("MONGO_PASSWORD"),
            database = requireEnv("MONGO_DATABASE"),
        )

    override val redisConfig: RedisConfig =
        RedisConfig(
            host = requireEnv("REDIS_HOST"),
            password = requireEnv("REDIS_PASSWORD"),
            port = requireEnv("REDIS_PORT"),
        )
    override val exposedConfig: ExposedConfig =
        ExposedConfig(
            url = requireEnv("DATABASE_URL"),
            driver = requireEnv("DATABASE_DRIVER"),
            user = requireEnv("DATABASE_USER"),
            password = requireEnv("DATABASE_PASSWORD"),
        )
    override val azureConfig: AzureConfig
        get() = TODO("Not yet implemented")
    override val jwtConfig: JwtConfig
        get() = TODO("Not yet implemented")
}
