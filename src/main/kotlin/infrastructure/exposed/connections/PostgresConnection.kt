package com.khrix.infrastructure.exposed.connections

import org.jetbrains.exposed.v1.jdbc.Database
import java.util.*

class PostgresConnection : DatabaseConnection() {
    private val properties: Properties by lazy {
        Properties().apply {
            this::class.java.classLoader.getResourceAsStream("secrets.properties")?.use {
                load(it)
            }
        }
    }

    override val database = Database.connect(
        url = properties.getProperty("db.url"),
        driver = properties.getProperty("db.driver"),
        user = properties.getProperty("db.user"),
        password = properties.getProperty("db.password")
    )
}