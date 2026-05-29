package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.CreateNewVehicleUseCase
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository

class CreateNewVehicleUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : CreateNewVehicleUseCase,
    BaseUseCaseImpl<Vehicle, Vehicle>() {
    override suspend fun internalExecute(command: Vehicle): Vehicle {
        return vehiclesRepository.createRead(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Create new vehicle and bind with it's owner"
    }
}
