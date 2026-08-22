package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.serviceorder.model.ServiceOrder

data class ServiceOrderWithHistory(
    val serviceOrder: ServiceOrder,
    val changes: List<HistoryChanges>,
)

interface GetServiceOrdersByCodeUseCase : BaseUseCase<String, ServiceOrderWithHistory>
