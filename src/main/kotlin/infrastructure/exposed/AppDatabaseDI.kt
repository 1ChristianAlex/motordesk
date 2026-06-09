package com.khrix.infrastructure.exposed

import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.infrastructure.exposed.address.repository.AddressExposedRepositoryImpl
import com.khrix.infrastructure.exposed.company.repository.CompanyExposedRepositoryImpl
import com.khrix.infrastructure.exposed.connections.MemoryConnection
import com.khrix.infrastructure.exposed.connections.PostgresConnection
import com.khrix.infrastructure.exposed.email.repository.EmailQueueExposedQueueRepositoryImpl
import com.khrix.infrastructure.exposed.inventory.repository.InventoryExposedRepositoryImpl
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import com.khrix.infrastructure.exposed.serviceorder.repository.ServiceOrderExposedRepositoryImpl
import com.khrix.infrastructure.exposed.serviceorder.repository.TaskExposedRepositoryImpl
import com.khrix.infrastructure.exposed.user.repository.UserExposedRepositoryImpl
import com.khrix.infrastructure.exposed.vehicles.repository.VehiclesExposedRepositoryImpl
import io.ktor.server.plugins.di.*
import org.jetbrains.exposed.v1.jdbc.Database

fun appDatabase(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(LoadSeeds::class)
        provide(MemoryConnection::class)
        provide(PostgresConnection::class)
        provide<Database> {
            resolve<PostgresConnection>().getConnection()
        }
        provide<AddressRepository>(AddressExposedRepositoryImpl::class)
        provide<UserRepository>(UserExposedRepositoryImpl::class)
        provide<CompanyRepository>(CompanyExposedRepositoryImpl::class)
        provide<VehiclesRepository>(VehiclesExposedRepositoryImpl::class)
        provide<TaskRepository>(TaskExposedRepositoryImpl::class)
        provide<InventoryRepository>(InventoryExposedRepositoryImpl::class)
        provide<ServiceOrderRepository>(ServiceOrderExposedRepositoryImpl::class)
        provide<EmailQueueRepository>(EmailQueueExposedQueueRepositoryImpl::class)
    }
}
