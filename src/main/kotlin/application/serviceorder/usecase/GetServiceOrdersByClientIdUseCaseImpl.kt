package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByClientIdUseCase

class GetServiceOrdersByClientIdUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : BaseUseCaseImpl<Int, List<ServiceOrder>>(),
    GetServiceOrdersByClientIdUseCase {
    override suspend fun internalExecute(command: Int): List<ServiceOrder> {
        val serviceOrder = serviceOrderRepository.getByClientId(command)

        return serviceOrder
    }

    override suspend fun useCaseDescription(): String = "Get service order using client Id"
}
