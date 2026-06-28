package com.khrix.infrastructure.app

class InfraCredentialsEnvImpl : InfraCredentials {
    private fun requireEnv(name: String): String =
        System.getenv(name)
            ?: throw Exception("Environment variable '$name' is required")

    override val mongoConfig: MongoConfig
        get() = MongoConfig(
            url = requireEnv("MONGO_URL"),
            username = requireEnv("MONGO_USER"),
            password = requireEnv("MONGO_PASSWORD"),
            database = requireEnv("MONGO_DATABASE"),
        ).run {
            println(this)

            this
        }

    override val redisConfig: RedisConfig
        get() = RedisConfig(
            host = requireEnv("REDIS_HOST"),
            password = requireEnv("REDIS_PASSWORD"),
            port = requireEnv("REDIS_PORT"),
        ).run {
            println(this)
            println(this.connectionString)

            this
        }
    override val exposedConfig: ExposedConfig
        get() = ExposedConfig(
            url = requireEnv("DATABASE_URL"),
            driver = requireEnv("DATABASE_DRIVER"),
            user = requireEnv("DATABASE_USER"),
            password = requireEnv("DATABASE_PASSWORD")
        ).run {
            println(this)

            this
        }
}