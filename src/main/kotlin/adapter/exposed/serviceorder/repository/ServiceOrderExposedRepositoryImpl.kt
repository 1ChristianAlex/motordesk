package com.khrix.adapter.exposed.serviceorder.repository

import com.khrix.adapter.exposed.BaseExposedRepository
import com.khrix.adapter.exposed.inventory.database.InventoryEntity
import com.khrix.adapter.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.adapter.exposed.serviceorder.database.ServiceOrderTasksTable
import com.khrix.adapter.exposed.serviceorder.database.ServiceOrdersTable
import com.khrix.adapter.exposed.serviceorder.database.TaskEntity
import com.khrix.adapter.exposed.serviceorder.mapper.toModel
import com.khrix.adapter.exposed.user.database.UserEntity
import com.khrix.adapter.exposed.vehicles.database.VehicleEntity
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.update

class ServiceOrderExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<ServiceOrderEntity, ServiceOrder>(database),
    ServiceOrderRepository {
    override suspend fun read(id: Int): ServiceOrder? =
        suspendedQuery {
            ServiceOrderEntity.findById(id)?.toModel()
        }

    override suspend fun update(
        id: Int,
        data: ServiceOrder,
    ) {
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
                it.totalAmount = data.totalPrice
            }
        }
    }

    override suspend fun create(data: ServiceOrder): Int = createServiceOrder(data).id.value

    private suspend fun createServiceOrder(data: ServiceOrder) =
        suspendedQuery {
            val order =
                ServiceOrderEntity.new {
                    client = UserEntity[data.client.id]
                    operator = UserEntity[data.operator.id]
                    tasks = TaskEntity.forIds(data.tasks.map { task -> task.id })
                    parts = InventoryEntity.forIds(data.inventoryItems.map { part -> part.id })
                    vehicle = VehicleEntity[data.vehicle.id]
                    status = data.status
                    complaint = data.complaint
                    diagnosis = data.diagnosis
                    totalAmount = data.totalPrice
                    code = data.code
                }

            order
        }

    override suspend fun delete(id: Int) {
        suspendedQuery {
            ServiceOrderEntity.findByIdAndUpdate(id) {
                it.status = ServiceOrderStatus.CANCELLED
            }
        }
    }

    override suspend fun createRead(data: ServiceOrder): ServiceOrder = createServiceOrder(data).toModel()

    override suspend fun getByClientId(clientId: Int): List<ServiceOrder> =
        suspendedQuery {
            ServiceOrderEntity.find { ServiceOrdersTable.client eq clientId }.map { it.toModel() }
        }

    override suspend fun getOrderByVehicle(vehicleId: Int): List<ServiceOrder> =
        suspendedQuery {
            ServiceOrderEntity.find { ServiceOrdersTable.vehicle eq vehicleId }.map { it.toModel() }
        }

    override suspend fun getByCode(code: String): ServiceOrder? =
        suspendedQuery {
            ServiceOrderEntity.find { ServiceOrdersTable.code eq code }.map { it.toModel() }.firstOrNull()
        }

    override suspend fun updateServiceOrderTask(
        id: Int,
        taskId: Int,
        taskStatus: TaskProgressStatus,
    ) = suspendedQuery {
        ServiceOrderTasksTable.update({
            (ServiceOrderTasksTable.task eq taskId) and (ServiceOrderTasksTable.serviceOrder eq id)
        }) {
            it[ServiceOrderTasksTable.status] = taskStatus
        }
    }
}
