package com.khrix.infrastructure.exposed

import com.khrix.domain.address.repository.AddressRepository
import com.khrix.infrastructure.exposed.address.repository.AddressExposedRepositoryImpl
import com.khrix.infrastructure.exposed.connections.MemoryConnection
import io.ktor.server.plugins.di.*
import org.jetbrains.exposed.v1.jdbc.Database

fun appDatabase(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(MemoryConnection::class)
        provide<Database> {
            resolve<MemoryConnection>().getConnection()
        }
        provide<AddressRepository> {
            AddressExposedRepositoryImpl(resolve())
        }
    }
}
