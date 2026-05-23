package com.khrix.infrastructure.exposed

import com.khrix.domain.address.repository.AddressRepository
import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.user.repository.UserRepository
import com.khrix.infrastructure.exposed.address.repository.AddressExposedRepositoryImpl
import com.khrix.infrastructure.exposed.company.repository.CompanyExposedRepositoryImpl
import com.khrix.infrastructure.exposed.connections.MemoryConnection
import com.khrix.infrastructure.exposed.connections.PostgresConnection
import com.khrix.infrastructure.exposed.user.repository.UserExposedRepositoryImpl
import io.ktor.server.plugins.di.*
import org.jetbrains.exposed.v1.jdbc.Database

fun appDatabase(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(MemoryConnection::class)
        provide(PostgresConnection::class)
        provide<Database> {
            resolve<PostgresConnection>().getConnection()
        }
        provide<AddressRepository>(AddressExposedRepositoryImpl::class)
        provide<UserRepository>(UserExposedRepositoryImpl::class)
        provide<CompanyRepository>(CompanyExposedRepositoryImpl::class)
    }
}
