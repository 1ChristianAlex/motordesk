package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.core.shortid.ShortId
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UpdateServiceOrderUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val getInventoryByListIdOrSkuUseCase: GetInventoryByListIdOrSkuUseCase,
    private val getTaskByListIdUseCase: GetTaskByListIdUseCase,
    private val shortId: ShortId
) : UpdateServiceOrderUseCase, BaseUseCaseImpl<UpdateServiceOrderCommand, Unit>() {
    override suspend fun internalExecute(command: UpdateServiceOrderCommand) {
        coroutineScope {
            val serviceOrder = serviceOrderRepository.read(command.serviceOrderId)
                ?: throw IllegalArgumentException("Service order not found")

            val newTasks = async {
                getTaskByListIdUseCase.execute(command.tasksIds).getOrThrow()
            }
            val newInventoryItems = async {
                getInventoryByListIdOrSkuUseCase.execute(command.inventoryItemsIds.map { it.toString() }).getOrThrow()
            }
            val newServiceOrder = serviceOrder
                .updateStatus(command.status)
                .updateComplaint(command.complaint)
                .updateDiagnosis(command.diagnosis)
                .updateTasks(newTasks.await())
                .updateInventoryItems(newInventoryItems.await()).apply {
                    code = shortId.encode(codeIds())
                }



            serviceOrderRepository.update(newServiceOrder.id, newServiceOrder)
        }
    }

    override suspend fun useCaseDescription(): String {
        return "Update service order"
    }
}
