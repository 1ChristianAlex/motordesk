package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.app.loadProperties
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import io.ktor.server.plugins.di.annotations.*
import org.jetbrains.exposed.v1.jdbc.Database
import java.util.*

class PostgresConnection(@Named("isDevelopment") isDevelopment: Boolean, loadSeeds: LoadSeeds) :
    DatabaseConnection(isDevelopment, loadSeeds) {
    private val properties: Properties by lazy {
        loadProperties()
    }

    override val database = Database.connect(
        url = properties.getProperty("db.url"),
        driver = properties.getProperty("db.driver"),
        user = properties.getProperty("db.user"),
        password = properties.getProperty("db.password")
    )
}