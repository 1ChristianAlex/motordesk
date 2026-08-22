package com.khrix.adapter.exposed.serviceorder.database

import com.khrix.adapter.exposed.inventory.database.InventoryTable
import org.jetbrains.exposed.v1.core.Table

object ServiceOrderPartsTable : Table(
    "serviceOrderParts",
) {
    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val part =
        reference("partId", InventoryTable)

    override val primaryKey = PrimaryKey(serviceOrder, part)

    init {
        index(true, serviceOrder, part)
    }
}
