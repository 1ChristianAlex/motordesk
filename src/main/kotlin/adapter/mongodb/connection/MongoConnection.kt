package com.khrix.adapter.mongodb.connection

import com.khrix.adapter.app.InfraConfig
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.slf4j.LoggerFactory

class MongoConnection(
    private val infraConfig: InfraConfig,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val client: MongoClient by lazy {
        try {
            logger.info("Starting mongo connection")
            MongoClient.create(infraConfig.mongoConfig.connectionString)
        } catch (e: Exception) {
            throw RuntimeException("Failed to create MongoDB client: ${e.message}", e)
        }
    }

    val database: MongoDatabase =
        try {
            client.getDatabase(infraConfig.mongoConfig.database)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get MongoDB database: ${e.message}", e)
        }

    fun close() {
        client.close()
    }
}
