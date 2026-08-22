package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrderHistoryUseCase
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.port.usecase.ServiceOrderWithHistory

class GetServiceOrdersByCodeUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val getServiceOrderHistoryUseCase: GetServiceOrderHistoryUseCase,
) : BaseUseCaseImpl<String, ServiceOrderWithHistory>(),
    GetServiceOrdersByCodeUseCase {
    override suspend fun internalExecute(command: String): ServiceOrderWithHistory {
        val serviceOrder = serviceOrderRepository.getByCode(command) ?: throw NoSuchElementException()

        val changes =
            getServiceOrderHistoryUseCase
                .execute(serviceOrder.id)
                .onFailure {
                    this.logger?.error(it.message, it)
                }.getOrNull()

        return ServiceOrderWithHistory(
            serviceOrder = serviceOrder,
            changes = changes ?: listOf(),
        )
    }

    override suspend fun useCaseDescription(): String = "Get service order using code"
}
