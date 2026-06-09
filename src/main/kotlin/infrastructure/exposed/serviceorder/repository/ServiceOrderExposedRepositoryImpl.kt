package com.khrix.infrastructure.exposed.serviceorder.repository

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.infrastructure.exposed.serviceorder.database.TaskEntity
import com.khrix.infrastructure.exposed.serviceorder.mapper.toModel
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

class ServiceOrderExposedRepositoryImpl(
    database: R2dbcDatabase,
) : BaseExposedRepository<ServiceOrderEntity, ServiceOrder>(database), ServiceOrderRepository {
    override suspend fun read(id: Int): ServiceOrder? {
        return suspendedQuery {
            ServiceOrderEntity.findById(id)?.toModel()
        }
    }

    override suspend fun update(id: Int, data: ServiceOrder) {
        suspendedQuery {
            ServiceOrderEntity.findByIdAndUpdate(id) {
                it.client = UserEntity[data.client.id]
                it.operator = UserEntity[data.operator.id]
                it.tasks = TaskEntity.forIds(data.tasks.map { task -> task.id })
                it.parts = InventoryEntity.forIds(data.inventoryItems.map { part -> part.id })
                it.vehicle = VehicleEntity[data.vehicle.id]
                it.status = data.status
                it.complaint = data.complaint
                it.diagnosis = data.diagnosis
                it.totalAmount = data.totalAmount
            }
        }
    }

    override suspend fun create(data: ServiceOrder): Int {
        return createTask(data).id.value
    }

    private suspend fun createTask(data: ServiceOrder) = suspendedQuery {
        ServiceOrderEntity.new {
            client = UserEntity[data.client.id]
            operator = UserEntity[data.operator.id]
            tasks = TaskEntity.forIds(data.tasks.map { task -> task.id })
            parts = InventoryEntity.forIds(data.inventoryItems.map { part -> part.id })
            vehicle = VehicleEntity[data.vehicle.id]
            status = data.status
            complaint = data.complaint
            diagnosis = data.diagnosis
            totalAmount = data.totalAmount
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery {
            ServiceOrderEntity.findByIdAndUpdate(id) {
                it.status = ServiceOrderStatus.CANCELLED
            }
        }
    }

    override suspend fun createRead(data: ServiceOrder): ServiceOrder {
        return createTask(data).toModel()
    }
}
