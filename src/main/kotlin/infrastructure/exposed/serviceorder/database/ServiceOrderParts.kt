package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.infrastructure.exposed.inventory.database.InventoryTable
import org.jetbrains.exposed.v1.core.Table

object ServiceOrderPartsTable : Table(
    "serviceOrderParts"
) {

    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val part =
        reference("partId", InventoryTable)

    override val primaryKey = PrimaryKey(serviceOrder, part)
}
