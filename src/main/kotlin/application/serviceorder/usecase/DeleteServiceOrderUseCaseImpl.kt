package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.port.usecase.DeleteServiceOrderCommand
import com.khrix.domain.serviceorder.port.usecase.DeleteServiceOrderUseCase

class DeleteServiceOrderUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : BaseUseCaseImpl<DeleteServiceOrderCommand, Unit>(),
    DeleteServiceOrderUseCase {
    override suspend fun internalExecute(command: DeleteServiceOrderCommand) {
        val serviceOrder =
            serviceOrderRepository.read(command.serviceOrderId)
                ?: throw NoSuchElementException("Service order not found")

        serviceOrderRepository.delete(serviceOrder.id)
    }

    override suspend fun useCaseDescription(): String = "Virtually delete a service order"
}
