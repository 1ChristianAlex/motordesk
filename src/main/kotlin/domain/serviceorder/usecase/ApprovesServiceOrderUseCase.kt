package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase

data class ApprovesServiceOrderCommand(
    val token: String,
    val orderCode: String,
)

interface ApprovesServiceOrderUseCase : BaseUseCase<ApprovesServiceOrderCommand, Unit>
