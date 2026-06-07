package com.khrix.domain.inventory.model

data class InventoryItem(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String?,
    val quantity: Int,
    val minimumQuantity: Int,
    val unitPrice: java.math.BigDecimal,
    val isActive: Boolean
)
