package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.port.ServiceOrderDiffResolver
import com.khrix.domain.serviceorder.port.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.port.usecase.CreateServiceOrderHistoryCommand
import com.khrix.domain.serviceorder.port.usecase.CreateServiceOrderHistoryUseCase

class CreateServiceOrderHistoryUseCaseImpl(
    private val serviceOrderHistoryRepository: ServiceOrderHistoryRepository,
    private val serviceOrderDiffResolver: ServiceOrderDiffResolver,
) : BaseUseCaseImpl<CreateServiceOrderHistoryCommand, Unit>(),
    CreateServiceOrderHistoryUseCase {
    override suspend fun internalExecute(command: CreateServiceOrderHistoryCommand) {
        val orderDiff = serviceOrderDiffResolver.shallowDiff(command.newOrder, command.oldOrder)

        serviceOrderHistoryRepository.create(orderDiff)
    }

    override suspend fun useCaseDescription(): String = "Create a new service order history item"
}
