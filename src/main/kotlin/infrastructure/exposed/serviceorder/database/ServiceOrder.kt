package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity
import com.khrix.infrastructure.exposed.vehicles.database.VehicleTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ServiceOrdersTable : BaseTable("serviceOrders") {

    val client = reference("clientId", UsersTable)

    val operator = reference("operatorId", UsersTable)

    val vehicle = reference("vehicleId", VehicleTable)

    val status = enumerationByName<ServiceOrderStatus>(
        "status",
        25
    ).default(ServiceOrderStatus.CREATED)

    val complaint = text("complaint")

    val diagnosis = text("diagnosis").nullable()

    val totalAmount = decimal("totalAmount", 12, 2)
        .default(java.math.BigDecimal.ZERO)

    val code = varchar("code", 25).uniqueIndex()
}

class ServiceOrderEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ServiceOrderEntity>(
        ServiceOrdersTable
    )

    var client by UserEntity referencedOn ServiceOrdersTable.client

    var operator by UserEntity referencedOn ServiceOrdersTable.operator

    var tasks by TaskEntity via ServiceOrderTasksTable
    var parts by InventoryEntity via ServiceOrderPartsTable

    var vehicle by VehicleEntity referencedOn ServiceOrdersTable.vehicle

    var status by ServiceOrdersTable.status

    var complaint by ServiceOrdersTable.complaint
    var code by ServiceOrdersTable.code
    var diagnosis by ServiceOrdersTable.diagnosis

    var totalAmount by ServiceOrdersTable.totalAmount

    var createdAt by ServiceOrdersTable.createdAt
    var updatedAt by ServiceOrdersTable.updatedAt
}