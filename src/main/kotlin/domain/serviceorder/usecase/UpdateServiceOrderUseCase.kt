package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrderStatus

data class UpdateServiceOrderCommand(
    val complaint: String?,
    val diagnosis: String? = null,
    val tasksIds: List<Int> = listOf(),
    val inventoryItemsIds: List<Int> = listOf(),
    val serviceOrderId: Int,
    val status: ServiceOrderStatus? = null,
)

interface UpdateServiceOrderUseCase : BaseUseCase<UpdateServiceOrderCommand, Unit>

