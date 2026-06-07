package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity
import com.khrix.infrastructure.exposed.vehicles.database.VehiclesTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ServiceOrdersTable : BaseTable("serviceOrders") {

    val customer = reference("customerId", UsersTable)

    val createdBy = reference("createdById", UsersTable)

    val vehicle = reference("vehicleId", VehiclesTable)

    val status = enumerationByName<ServiceOrderStatus>(
        "status",
        25
    ).default(ServiceOrderStatus.CREATED)

    val complaint = text("complaint")

    val diagnosis = text("diagnosis").nullable()

    val totalAmount = decimal("totalAmount", 12, 2)
        .default(java.math.BigDecimal.ZERO)
}

class ServiceOrderEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ServiceOrderEntity>(
        ServiceOrdersTable
    )

    var customer by UserEntity referencedOn ServiceOrdersTable.customer

    var createdBy by UserEntity referencedOn ServiceOrdersTable.createdBy

    var vehicle by VehicleEntity referencedOn ServiceOrdersTable.vehicle

    var status by ServiceOrdersTable.status

    var complaint by ServiceOrdersTable.complaint

    var diagnosis by ServiceOrdersTable.diagnosis

    var totalAmount by ServiceOrdersTable.totalAmount

    var createdAt by ServiceOrdersTable.createdAt
    var updatedAt by ServiceOrdersTable.updatedAt
}