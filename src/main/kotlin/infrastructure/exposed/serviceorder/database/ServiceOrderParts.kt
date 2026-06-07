package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.inventory.database.InventoryTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ServiceOrderPartsTable : BaseTable(
    "serviceOrderParts"
) {

    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val part =
        reference("partId", InventoryTable)

    val quantity = integer("quantity")

    val unitPrice = decimal("unitPrice", 12, 2)
}

class ServiceOrderPartEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object :
        IntEntityClass<ServiceOrderPartEntity>(
            ServiceOrderPartsTable
        )

    var serviceOrder by ServiceOrderEntity referencedOn
            ServiceOrderPartsTable.serviceOrder

    var part by InventoryEntity referencedOn
            ServiceOrderPartsTable.part

    var quantity by ServiceOrderPartsTable.quantity

    var unitPrice by ServiceOrderPartsTable.unitPrice

    var createdAt by ServiceOrderPartsTable.createdAt
    var updatedAt by ServiceOrderPartsTable.updatedAt
}