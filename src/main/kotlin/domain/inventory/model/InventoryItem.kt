package com.khrix.domain.inventory.model

import com.khrix.domain.valueobject.Price

data class InventoryItem(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String?,
    val quantity: Int,
    val minimumQuantity: Int,
    val unitPrice: Price,
    val isActive: Boolean
)
