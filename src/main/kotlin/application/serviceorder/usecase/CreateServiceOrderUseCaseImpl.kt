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
    private val shortId: ShortId,
    private val serviceOrderHistoryRepository: ServiceOrderHistoryRepository,
) : BaseUseCaseImpl<CreateServiceOrderCommand, ServiceOrder>(),
    CreateServiceOrderUseCase {
    override suspend fun internalExecute(command: CreateServiceOrderCommand): ServiceOrder =
        coroutineScope {
            val client = async { getUserUseCase.execute(command.clientId).getOrThrow() }
            val operator = async { getUserUseCase.execute(command.operatorId).getOrThrow() }.await()
            val vehicle = async { getVehicleByIdUseCase.execute(command.vehicleId).getOrThrow() }
            val tasks = async { getTaskByListIdUseCase.execute(command.tasksIds).getOrThrow() }
            val inventoryItems =
                async {
                    getInventoryByListIdOrSkuUseCase
                        .execute(command.inventoryItemsIds.map { it.toString() })
                        .getOrThrow()
                }

            val serviceOrder =
                ServiceOrder(
                    client = client.await(),
                    operator = operator,
                    vehicle = vehicle.await(),
                    complaint = command.complaint,
                    diagnosis = command.diagnosis,
                    tasks = tasks.await(),
                    inventoryItems = inventoryItems.await(),
                    status = ServiceOrderStatus.CREATED,
                    id = 0,
                ).apply {
                    code = shortId.encode(this.codeIds())
                }

            val orderAlreadyExists = serviceOrderRepository.getByCode(serviceOrder.code)

            if (orderAlreadyExists != null) {
                throw Exception("Service Order matching the required params already exists")
            }
            val order = serviceOrderRepository.createRead(serviceOrder)

            launch { createEmailQueueUseCase.execute(serviceOrder) }
            launch { serviceOrderHistoryRepository.create(serviceOrder) }

            order
        }

    override suspend fun useCaseDescription(): String = "Create a new service order"
}
