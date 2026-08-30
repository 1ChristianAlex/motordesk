package com.khrix.domain.serviceorder.port.usecase

import com.khrix.domain.core.BaseUseCase

data class ApprovesServiceOrderCommand(
    val token: String,
    val orderCode: String,
)

interface ApprovesServiceOrderUseCase : BaseUseCase<ApprovesServiceOrderCommand, Unit>
