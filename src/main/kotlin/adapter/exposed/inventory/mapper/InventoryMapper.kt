package com.khrix.adapter.exposed.inventory.mapper

import com.khrix.adapter.exposed.inventory.database.InventoryEntity
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.valueobject.Price

fun InventoryEntity.toModel(): InventoryItem =
    InventoryItem(
        sku = this.sku,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        minimumQuantity = this.minimumQuantity,
        unitPrice = Price(this.unitPrice),
        isActive = this.isActive,
        id = this.id.value,
    )
