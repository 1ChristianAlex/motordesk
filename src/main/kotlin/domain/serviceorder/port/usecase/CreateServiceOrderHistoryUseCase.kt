package com.khrix.domain.serviceorder.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

data class CreateServiceOrderHistoryCommand(
    val newOrder: ServiceOrder,
    val oldOrder: ServiceOrder,
)

interface CreateServiceOrderHistoryUseCase : BaseUseCase<CreateServiceOrderHistoryCommand, Unit>
