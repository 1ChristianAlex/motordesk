package com.khrix.domain.serviceorder.model

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.user.model.User
import com.khrix.domain.vehicle.model.Vehicle
import java.math.BigDecimal

data class ServiceOrder(
    val client: User,
    val operator: User,
    val vehicle: Vehicle,
    val status: ServiceOrderStatus,
    val complaint: String,
    val diagnosis: String? = null,
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    val tasks: List<Task>,
    val inventoryItems: List<InventoryItem> = listOf()
)