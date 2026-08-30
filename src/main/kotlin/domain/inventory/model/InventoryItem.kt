package com.khrix.domain.inventory.model

import com.khrix.domain.valueobject.Price
import kotlinx.serialization.Serializable

@Serializable
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
