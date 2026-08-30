package com.khrix.infrastructure.http.controllers.serviceorder.resources.dto

import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.user.model.Role
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ServiceOrderInputDto(
    val clientId: Int,
    val operatorId: Int,
    val vehicleId: Int,
    val complaint: String,
    val diagnosis: String? = null,
    val tasksIds: List<Int>,
    val inventoryItemsIds: List<Int> = listOf()
)

@Serializable
data class UpdateServiceOrderInputDto(
    val code: String,
    val complaint: String? = null,
    val diagnosis: String? = null,
    val tasksIds: List<Int> = listOf(),
    val inventoryItemsIds: List<Int> = listOf(),
    val status: ServiceOrderStatus? = null,
) {
    lateinit var operatorRole: Role
        private set

    fun setOperatorRole(role: Role) {
        this.operatorRole = role
    }
}

data class ClientServiceOrderItemInputDto(val clientId: Int, val code: String)

@Serializable
data class ServiceOrderOutputDto(
    val id: Int,
    val client: UserOutputDto,
    val operator: UserOutputDto,
    val vehicle: VehicleOutputDto,
    val status: ServiceOrderStatus,
    val complaint: String,
    val code: String,
    val expectedMinutes: Int,
    @Serializable(with = DecimalAsStringSerializer::class)
    val totalPrice: BigDecimal,
    val diagnosis: String? = null,
    val tasks: List<TaskOutputDto>,
    val inventoryItems: List<InventoryItemOutputDto> = listOf()
)