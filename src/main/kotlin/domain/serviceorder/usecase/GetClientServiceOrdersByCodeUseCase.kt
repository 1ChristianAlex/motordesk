package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase

data class GetClientServiceOrdersByCodeCommand(
    val code: String,
    val userId: Int,
)

interface GetClientServiceOrdersByCodeUseCase : BaseUseCase<GetClientServiceOrdersByCodeCommand, ServiceOrderWithHistory>
