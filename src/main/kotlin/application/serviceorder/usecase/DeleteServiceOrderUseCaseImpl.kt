package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.DeleteServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.DeleteServiceOrderUseCase

class DeleteServiceOrderUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : DeleteServiceOrderUseCase, BaseUseCaseImpl<DeleteServiceOrderCommand, Unit>() {
    override suspend fun internalExecute(command: DeleteServiceOrderCommand) {
        val serviceOrder = serviceOrderRepository.read(command.serviceOrderId)
            ?: throw NoSuchElementException("Service order not found")

        serviceOrderRepository.delete(serviceOrder.id)
    }

    override suspend fun useCaseDescription(): String {
        return "Virtually delete a service order"
    }
}
