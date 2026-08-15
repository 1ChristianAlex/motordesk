package com.khrix.infrastructure.mongodb.connection

import com.khrix.infrastructure.app.InfraCredentials
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class MongoConnection(
    private val infraCredentials: InfraCredentials,
) {
    private val client: MongoClient by lazy {
        try {
            MongoClient.create(infraCredentials.mongoConfig.connectionString)
        } catch (e: Exception) {
            throw RuntimeException("Failed to create MongoDB client: ${e.message}", e)
        }
    }

    val database: MongoDatabase =
        try {
            client.getDatabase(infraCredentials.mongoConfig.database)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get MongoDB database: ${e.message}", e)
        }

    fun close() {
        client.close()
    }
}
