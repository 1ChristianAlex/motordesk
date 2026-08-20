package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderTaskHistoryRepository
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderHistoryUseCase

class CreateServiceOrderHistoryUseCaseImpl(
    private val serviceOrderHistoryRepository: ServiceOrderHistoryRepository,
    private val serviceOrderTaskHistoryRepository: ServiceOrderTaskHistoryRepository,
) : BaseUseCaseImpl<ServiceOrder, Unit>(),
    CreateServiceOrderHistoryUseCase {
    override suspend fun internalExecute(command: ServiceOrder) {
        serviceOrderHistoryRepository.create(command)

//        serviceOrderTaskHistoryRepository.create(command.toServiceOrderTasks())
    }

    override suspend fun useCaseDescription(): String = "Create a new service order"
}
