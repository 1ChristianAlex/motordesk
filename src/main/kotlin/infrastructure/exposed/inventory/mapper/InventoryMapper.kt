package com.khrix.infrastructure.exposed.inventory.mapper

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity

fun InventoryEntity.toModel(): InventoryItem = InventoryItem(
    sku = this.sku,
    name = this.name,
    description = this.description,
    quantity = this.quantity,
    minimumQuantity = this.minimumQuantity,
    unitPrice = this.unitPrice,
    isActive = this.isActive,
    id = this.id.value
)

