package com.khrix.adapter.outbound.exposed.inventory.database

import com.khrix.adapter.outbound.exposed.BaseTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object InventoryTable : BaseTable("inventory") {
    val sku = varchar("sku", 50).uniqueIndex()

    val name = varchar("name", 150)

    val description = text("description").nullable()

    val quantity = integer("quantity")

    val minimumQuantity = integer("minimumQuantity").default(0)

    val unitPrice = decimal("unitPrice", 12, 2)

    val isActive = bool("isActive").default(true)
}

class InventoryEntity(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<InventoryEntity>(InventoryTable)

    var sku by InventoryTable.sku

    var name by InventoryTable.name

    var description by InventoryTable.description

    var quantity by InventoryTable.quantity

    var minimumQuantity by InventoryTable.minimumQuantity

    var unitPrice by InventoryTable.unitPrice

    var isActive by InventoryTable.isActive

    var createdAt by InventoryTable.createdAt
    var updatedAt by InventoryTable.updatedAt
}
