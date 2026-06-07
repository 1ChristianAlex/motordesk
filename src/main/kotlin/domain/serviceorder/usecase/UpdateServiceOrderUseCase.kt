package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase

data class UpdateServiceOrderCommand(
    val complaint: String,
    val diagnosis: String? = null,
    val tasksIds: List<Int>,
    val inventoryItemsIds: List<Int> = listOf(),
    val serviceOrderId: Int
)

interface UpdateServiceOrderUseCase : BaseUseCase<UpdateServiceOrderCommand, Unit>

