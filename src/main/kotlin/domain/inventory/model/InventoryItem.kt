package com.khrix.domain.inventory.model

import com.khrix.domain.valueobject.Price
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class InventoryItem(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String?,
    val quantity: Int,
    val minimumQuantity: Int,
    val unitPrice: Price,
    val isActive: Boolean,
) {
    val total: BigDecimal
        get() {
            return BigDecimal.ZERO.add(unitPrice.value.multiply(BigDecimal(quantity)))
        }
}
