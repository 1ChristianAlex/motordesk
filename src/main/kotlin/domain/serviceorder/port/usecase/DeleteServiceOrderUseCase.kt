package com.khrix.domain.serviceorder.port.usecase

import com.khrix.domain.core.BaseUseCase

data class DeleteServiceOrderCommand(
    val complaint: String,
    val serviceOrderId: Int,
)

interface DeleteServiceOrderUseCase : BaseUseCase<DeleteServiceOrderCommand, Unit>
