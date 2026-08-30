package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeCommand
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase

class GetClientServiceOrdersByCodeUseCaseImpl(
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase,
) : GetClientServiceOrdersByCodeUseCase, BaseUseCaseImpl<GetClientServiceOrdersByCodeCommand, ServiceOrder>() {
    override suspend fun internalExecute(command: GetClientServiceOrdersByCodeCommand): ServiceOrder {
        val serviceOrder = getServiceOrdersByCodeUseCase.execute(command.code).getOrThrow()

        if (serviceOrder.client.id != command.userId) {
            throw UnsupportedOperationException("You can only search for service order that belongs to you")
        }

        return serviceOrder
    }

    override suspend fun useCaseDescription(): String {
        return "Get service order using code"
    }
}
