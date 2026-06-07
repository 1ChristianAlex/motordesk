package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

data class DeleteServiceOrderCommand(
    val complaint: String,
    val serviceOrderId: Int,
)

interface DeleteServiceOrderUseCase : BaseUseCase<DeleteServiceOrderCommand, ServiceOrder>