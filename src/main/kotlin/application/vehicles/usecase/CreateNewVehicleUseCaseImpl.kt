package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.CreateNewVehicleUseCase

class CreateNewVehicleUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : CreateNewVehicleUseCase,
    BaseUseCaseImpl<Vehicle, Vehicle>() {
    override suspend fun internalExecute(command: Vehicle): Vehicle {
        val existingVehicle = vehiclesRepository.getByPlateOrChassis(command.plate.value, command.chassis)
        if (existingVehicle != null) {
            throw Exception("Vehicle with the same plate or chassis already exists")
        }

        return vehiclesRepository.createRead(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Create new vehicle and bind with it's owner"
    }
}
