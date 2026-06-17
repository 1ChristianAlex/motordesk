package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CreateServiceOrderUseCaseImpl(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val getInventoryByListIdOrSkuUseCase: GetInventoryByListIdOrSkuUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getVehicleByIdUseCase: GetVehicleByIdUseCase,
    private val getTaskByListIdUseCase: GetTaskByListIdUseCase,
    private val createEmailQueueUseCase: CreateEmailQueueUseCase,
) : CreateServiceOrderUseCase, BaseUseCaseImpl<CreateServiceOrderCommand, ServiceOrder>() {
    override suspend fun internalExecute(command: CreateServiceOrderCommand): ServiceOrder {
        return coroutineScope {
            val client = async { getUserUseCase.execute(command.clientId).getOrThrow() }
            val operator = async { getUserUseCase.execute(command.operatorId).getOrThrow() }.await()
            val vehicle = async { getVehicleByIdUseCase.execute(command.vehicleId).getOrThrow() }
            val tasks = async { getTaskByListIdUseCase.execute(command.tasksIds).getOrThrow() }
            val inventoryItems = async {
                getInventoryByListIdOrSkuUseCase.execute(command.inventoryItemsIds.map { it.toString() }).getOrThrow()
            }

            val serviceOrder = ServiceOrder(
                client = client.await(),
                operator = operator,
                vehicle = vehicle.await(),
                complaint = command.complaint,
                diagnosis = command.diagnosis,
                tasks = tasks.await(),
                inventoryItems = inventoryItems.await(),
                status = ServiceOrderStatus.CREATED,
                id = 0
            )

            val order = serviceOrderRepository.createRead(serviceOrder)

            launch { createEmailQueueUseCase.execute(serviceOrder) }

            order
        }
    }

    override suspend fun useCaseDescription(): String {
        return "Create a new service order"
    }
}
