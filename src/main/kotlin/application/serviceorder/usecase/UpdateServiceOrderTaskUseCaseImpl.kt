package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderTaskCommand
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderTaskUseCase

class UpdateServiceOrderTaskUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : BaseUseCaseImpl<UpdateServiceOrderTaskCommand, Unit>(),
    UpdateServiceOrderTaskUseCase {
    override suspend fun internalExecute(command: UpdateServiceOrderTaskCommand) {
        serviceOrderRepository.updateServiceOrderTask(command.serviceOrderId, command.taskId, command.status)
    }

    override suspend fun useCaseDescription(): String = "Update tasks from service order"
}
