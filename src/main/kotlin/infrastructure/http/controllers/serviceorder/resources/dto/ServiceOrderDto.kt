package com.khrix.infrastructure.http.controllers.serviceorder.resources.dto

import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import kotlinx.serialization.Serializable

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
data class ServiceOrderOutputDto(
    val id: Int,
    val client: UserOutputDto,
    val operator: UserOutputDto,
    val vehicle: VehicleOutputDto,
    val status: ServiceOrderStatus,
    val complaint: String,
    val diagnosis: String? = null,
    val tasks: List<TaskOutputDto>,
    val inventoryItems: List<InventoryItemOutputDto> = listOf()
)