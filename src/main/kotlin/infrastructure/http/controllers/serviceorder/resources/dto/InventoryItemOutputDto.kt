package com.khrix.infrastructure.http.controllers.serviceorder.resources.dto

import com.khrix.domain.valueobject.Price
import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemOutputDto(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String?,
    val quantity: Int,
    val minimumQuantity: Int,
    val unitPrice: Price,
    val isActive: Boolean
)