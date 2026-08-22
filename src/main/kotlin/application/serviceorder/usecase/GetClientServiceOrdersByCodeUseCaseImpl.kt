package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeCommand
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.ServiceOrderWithHistory

class GetClientServiceOrdersByCodeUseCaseImpl(
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase,
) : BaseUseCaseImpl<GetClientServiceOrdersByCodeCommand, ServiceOrderWithHistory>(),
    GetClientServiceOrdersByCodeUseCase {
    override suspend fun internalExecute(command: GetClientServiceOrdersByCodeCommand): ServiceOrderWithHistory {
        val serviceOrderWithHistory = getServiceOrdersByCodeUseCase.execute(command.code).getOrThrow()

        if (serviceOrderWithHistory.serviceOrder.client.id != command.userId) {
            throw UnsupportedOperationException("You can only search for service order that belongs to you")
        }

        return serviceOrderWithHistory
    }

    override suspend fun useCaseDescription(): String = "Get service order using code"
}
