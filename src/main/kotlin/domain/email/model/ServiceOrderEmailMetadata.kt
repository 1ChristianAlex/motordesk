package com.khrix.domain.email.model

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.user.model.Role
import com.khrix.domain.vehicle.model.Vehicle
import java.math.BigDecimal

data class UserEmailMetadata(
    val id: Int,
    val address: AddressEmailMetadata,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val role: Role,
)

data class AddressEmailMetadata(
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String
)

data class ServiceOrderEmailMetadata(
    val client: UserEmailMetadata,
    val operator: UserEmailMetadata,
    val vehicle: Vehicle,
    val status: ServiceOrderStatus,
    val complaint: String,
    val diagnosis: String? = null,
    val tasks: List<Task>,
    val inventoryItems: List<InventoryItem> = listOf(),
    val totalAmount: BigDecimal
)
