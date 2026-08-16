package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

data class CreateServiceOrderCommand(
    val clientId: Int,
    val operatorId: Int,
    val vehicleId: Int,
    val complaint: String,
    val diagnosis: String? = null,
    val tasksIds: List<Int>,
    val inventoryItemsIds: List<Int> = listOf(),
)

interface CreateServiceOrderUseCase : BaseUseCase<CreateServiceOrderCommand, ServiceOrder>
