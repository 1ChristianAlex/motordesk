package com.khrix.adapter.outbound.exposed.serviceorder.database

import com.khrix.adapter.outbound.exposed.inventory.database.InventoryTable
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
