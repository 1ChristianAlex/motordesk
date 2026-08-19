package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

data class GetClientServiceOrdersByCodeCommand(
    val code: String,
    val userId: Int,
)

interface GetClientServiceOrdersByCodeUseCase : BaseUseCase<GetClientServiceOrdersByCodeCommand, ServiceOrder>
