package com.khrix.infrastructure.app

class InfraCredentialsEnvImpl : InfraCredentials {
    override val mongoConfig: MongoConfig
        get() = MongoConfig(
            url = System.getenv("MONGO_URL"),
            username = System.getenv("MONGO_USER"),
            password = System.getenv("MONGO_PASSWORD"),
            database = System.getenv("MONGO_DATABASE"),
        )

    override val redisConfig: RedisConfig
        get() = RedisConfig(
            host = System.getenv("REDIS_HOST"),
            username = System.getenv("REDIS_USER"),
            password = System.getenv("REDIS_PASSWORD"),
            port = System.getenv("REDIS_PORT"),
        )
    override val exposedConfig: ExposedConfig
        get() = ExposedConfig(
            url = System.getenv("DATABASE_URL"),
            driver = System.getenv("DATABASE_DRIVER"),
            user = System.getenv("DATABASE_USER"),
            password = System.getenv("DATABASE_PASSWORD")
        )

    init {
        println(this.redisConfig)
    }
}