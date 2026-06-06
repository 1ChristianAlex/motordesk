package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase

class DeleteVehicleByIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : DeleteVehicleByIdUseCase,
    BaseUseCaseImpl<Int, Unit>() {
    override suspend fun internalExecute(command: Int): Unit {
        val vehicleExists = vehiclesRepository.read(command) != null
        if (!vehicleExists) {
            throw Exception("Vehicle with id $command does not exist")
        }
        return vehiclesRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Delete vehicle by Id"
    }
}
