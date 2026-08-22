package com.khrix.adapter.outbound.exposed

import com.khrix.adapter.outbound.exposed.address.repository.AddressExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.company.repository.CompanyExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.connections.MemoryConnection
import com.khrix.adapter.outbound.exposed.connections.PostgresConnection
import com.khrix.adapter.outbound.exposed.email.repository.EmailQueueExposedQueueRepositoryImpl
import com.khrix.adapter.outbound.exposed.inventory.repository.InventoryExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.seeds.LoadSeeds
import com.khrix.adapter.outbound.exposed.serviceorder.repository.ServiceOrderApprovalExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.serviceorder.repository.ServiceOrderExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.serviceorder.repository.TaskExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.user.repository.UserExposedRepositoryImpl
import com.khrix.adapter.outbound.exposed.vehicles.repository.VehiclesExposedRepositoryImpl
import com.khrix.domain.company.port.repository.CompanyRepository
import com.khrix.domain.email.port.repository.EmailQueueRepository
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.serviceorder.port.repository.ServiceOrderApprovalRepository
import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
import com.khrix.domain.user.address.port.repository.AddressRepository
import com.khrix.domain.user.port.repository.UserRepository
import com.khrix.domain.vehicle.port.repository.VehiclesRepository
import io.ktor.server.plugins.di.DependencyRegistry
import io.ktor.server.plugins.di.resolve
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
        provide<ServiceOrderApprovalRepository>(ServiceOrderApprovalExposedRepositoryImpl::class)
    }
}
