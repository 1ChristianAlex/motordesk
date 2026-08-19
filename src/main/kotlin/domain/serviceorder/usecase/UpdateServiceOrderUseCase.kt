package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.user.model.Role

data class UpdateServiceOrderCommand(
    val code: String,
    val complaint: String?,
    val diagnosis: String? = null,
    val tasksIds: List<Int> = listOf(),
    val inventoryItemsIds: List<Int> = listOf(),
    val status: ServiceOrderStatus? = null,
    val operatorRole: Role,
)

interface UpdateServiceOrderUseCase : BaseUseCase<UpdateServiceOrderCommand, Unit>
