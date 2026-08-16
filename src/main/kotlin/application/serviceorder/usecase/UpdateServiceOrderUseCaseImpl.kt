package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.core.shortid.ShortId
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.user.model.Role
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UpdateServiceOrderUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val getInventoryByListIdOrSkuUseCase: GetInventoryByListIdOrSkuUseCase,
    private val getTaskByListIdUseCase: GetTaskByListIdUseCase,
    private val shortId: ShortId,
    private val serviceOrderHistoryRepository: ServiceOrderHistoryRepository,
    private val createEmailQueueUseCase: CreateEmailQueueUseCase,
) : BaseUseCaseImpl<UpdateServiceOrderCommand, Unit>(),
    UpdateServiceOrderUseCase {
    override suspend fun internalExecute(command: UpdateServiceOrderCommand) {
        coroutineScope {
            val serviceOrder =
                serviceOrderRepository.getByCode(command.code)
                    ?: throw IllegalArgumentException("Service order not found")

            val newTasks =
                async {
                    getTaskByListIdUseCase.execute(command.tasksIds).getOrThrow()
                }
            val newInventoryItems =
                async {
                    getInventoryByListIdOrSkuUseCase
                        .execute(command.inventoryItemsIds.map { it.toString() })
                        .getOrThrow()
                }

            val newServiceOrder =
                serviceOrder
                    .updateStatus(command.status, command.operatorRole)
                    .updateComplaint(command.complaint)
                    .updateDiagnosis(command.diagnosis)
                    .updateTasks(newTasks.await())
                    .updateInventoryItems(newInventoryItems.await())
                    .apply {
                        code = shortId.encode(codeIds())
                    }

            serviceOrderRepository.update(newServiceOrder.id, newServiceOrder)

            if (needToSendEmail(serviceOrder, newServiceOrder)) {
                launch { createEmailQueueUseCase.execute(newServiceOrder) }
                launch {
                    serviceOrderRepository.update(
                        newServiceOrder.id,
                        newServiceOrder.updateStatus(
                            ServiceOrderStatus.QUEUED,
                            Role.MANAGER,
                        ),
                    )
                }
            }

            launch { serviceOrderHistoryRepository.create(newServiceOrder) }
        }
    }

    private fun needToSendEmail(
        oldServiceOrder: ServiceOrder,
        newServiceOrder: ServiceOrder,
    ): Boolean {
        val isTotalPriceDiff = oldServiceOrder.totalPrice != newServiceOrder.totalPrice
        val isTasksDiff = oldServiceOrder.tasks.map { it.id }.sorted() != newServiceOrder.tasks.map { it.id }.sorted()
        val isPartsDiff =
            oldServiceOrder.inventoryItems.map { it.id }.sorted() !=
                newServiceOrder.inventoryItems
                    .map { it.id }
                    .sorted()
        val isStatusDiff = oldServiceOrder.status != newServiceOrder.status

        return isTotalPriceDiff || isTasksDiff || isPartsDiff || isStatusDiff
    }

    override suspend fun useCaseDescription(): String = "Update service order"
}
