package com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.InventoryItemOutputDto

fun InventoryItem.toOutputDto(): InventoryItemOutputDto =
    InventoryItemOutputDto(
        id = this.id,
        sku = this.sku,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        minimumQuantity = this.minimumQuantity,
        unitPrice = this.unitPrice,
        isActive = this.isActive,
    )
