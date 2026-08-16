package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase

class DeleteVehicleByIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
) : BaseUseCaseImpl<Int, Unit>(),
    DeleteVehicleByIdUseCase {
    override suspend fun internalExecute(command: Int) {
        val vehicleExists =
            vehiclesRepository.read(command) ?: throw Exception("Vehicle with id $command does not exist")

        val servicerOrderList = serviceOrderRepository.getOrderByVehicle(vehicleExists.id)

        val statusAllowToDelete =
            listOf(
                ServiceOrderStatus.CANCELLED,
                ServiceOrderStatus.FINISHED,
            )
        if (servicerOrderList.any { it.status !in statusAllowToDelete }) {
            throw Exception("Vehicle can not be deleted while having an active Service Order")
        }

        return vehiclesRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String = "Delete vehicle by Id"
}
