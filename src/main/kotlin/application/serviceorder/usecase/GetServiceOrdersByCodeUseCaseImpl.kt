package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase

class GetServiceOrdersByCodeUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : GetServiceOrdersByCodeUseCase, BaseUseCaseImpl<String, ServiceOrder>() {
    override suspend fun internalExecute(command: String): ServiceOrder {
        val serviceOrder = serviceOrderRepository.getByCode(command)

        return serviceOrder ?: throw NoSuchElementException()
    }

    override suspend fun useCaseDescription(): String {
        return "Get service order using code"
    }
}
