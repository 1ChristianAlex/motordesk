package com.khrix.infrastructure.mongodb.connection

import com.khrix.infrastructure.app.loadProperties
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import java.util.*

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

class MongoConnection {
    private val properties: Properties by lazy {
        loadProperties()
    }

    private val mongoConfig = MongoConfig(
        properties.getProperty("mongo.url"),
        properties.getProperty("mongo.user"),
        properties.getProperty("mongo.password"),
        properties.getProperty("mongo.database"),
    )

    private val client: MongoClient by lazy {
        MongoClient.create(mongoConfig.connectionString)
    }

    val database: MongoDatabase by lazy {
        client.getDatabase(mongoConfig.database)
    }

    fun close() {
        client.close()
    }
}
