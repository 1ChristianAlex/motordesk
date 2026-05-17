package com.khrix.infrastructure.exposed

import com.khrix.infrastructure.exposed.connections.MemoryConnection
import io.ktor.server.plugins.di.*
import org.jetbrains.exposed.v1.jdbc.Database

fun appDatabase(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<Database> {
            MemoryConnection.getConnection()
        }
    }
}
