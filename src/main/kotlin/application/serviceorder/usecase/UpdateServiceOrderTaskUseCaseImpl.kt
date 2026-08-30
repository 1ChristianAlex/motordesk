package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderTaskCommand
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderTaskUseCase

class UpdateServiceOrderTaskUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
) : UpdateServiceOrderTaskUseCase, BaseUseCaseImpl<UpdateServiceOrderTaskCommand, Unit>() {
    override suspend fun internalExecute(command: UpdateServiceOrderTaskCommand) {
        serviceOrderRepository.updateServiceOrderTask(command.serviceOrderId, command.taskId, command.status)
    }

    override suspend fun useCaseDescription(): String {
        return "Update tasks from service order"
    }
}
