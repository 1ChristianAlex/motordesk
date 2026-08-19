package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderApprovalRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.ApprovesServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.ApprovesServiceOrderUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.user.model.Role
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ApprovesServiceOrderUseCaseImpl(
    private val serviceOrderApprovalRepository: ServiceOrderApprovalRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val updateServiceOrderUseCase: UpdateServiceOrderUseCase,
) : BaseUseCaseImpl<ApprovesServiceOrderCommand, Unit>(),
    ApprovesServiceOrderUseCase {
    override suspend fun internalExecute(command: ApprovesServiceOrderCommand) {
        val approvalItem =
            serviceOrderApprovalRepository.getByToken(command.token)
                ?: throw NoSuchElementException("Service order approval not found")

        if (!approvalItem.isAvailableToApprove()) {
            throw IllegalArgumentException("Service order approval is not available to approve")
        }

        val serviceOrder =
            serviceOrderRepository.getByCode(approvalItem.serviceOrderCode)
                ?: throw NoSuchElementException("Service order not found")

        if (command.orderCode != serviceOrder.code) {
            throw IllegalArgumentException("Service order approval is not matching the right data")
        }

        coroutineScope {
            launch {
                approvalItem.setUsed()
                serviceOrderApprovalRepository.update(approvalItem.id, approvalItem)
            }
            launch {
                updateServiceOrderUseCase.execute(
                    UpdateServiceOrderCommand(
                        code = serviceOrder.code,
                        complaint = serviceOrder.complaint,
                        diagnosis = serviceOrder.diagnosis,
                        status = ServiceOrderStatus.IN_PROGRESS,
                        operatorRole = Role.MANAGER,
                    ),
                )
            }
        }
    }

    override suspend fun useCaseDescription(): String = "Approves service order given a token"
}
