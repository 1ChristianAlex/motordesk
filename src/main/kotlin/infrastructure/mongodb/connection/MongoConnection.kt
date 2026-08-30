package com.khrix.infrastructure.mongodb.connection

import com.khrix.infrastructure.app.InfraCredentials
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class MongoConnection(
    private val infraCredentials: InfraCredentials,
) {
    private val client: MongoClient by lazy {
        MongoClient.create(infraCredentials.mongoConfig.connectionString)
    }

    val database: MongoDatabase = client.getDatabase(infraCredentials.mongoConfig.database)

    fun close() {
        client.close()
    }
}
