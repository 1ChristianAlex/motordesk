package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.infrastructure.exposed.BaseTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ServiceOrderServicesTable : BaseTable(
    "serviceOrderServices"
) {

    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val service =
        reference("serviceId", ServicesTable)

    val quantity = integer("quantity").default(1)

    val unitPrice = decimal("unitPrice", 12, 2)
}

class ServiceOrderServiceEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object :
        IntEntityClass<ServiceOrderServiceEntity>(
            ServiceOrderServicesTable
        )

    var serviceOrder by ServiceOrderEntity referencedOn
            ServiceOrderServicesTable.serviceOrder

    var service by ServiceEntity referencedOn
            ServiceOrderServicesTable.service

    var quantity by ServiceOrderServicesTable.quantity

    var unitPrice by ServiceOrderServicesTable.unitPrice

    var createdAt by ServiceOrderServicesTable.createdAt
    var updatedAt by ServiceOrderServicesTable.updatedAt
}