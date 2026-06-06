package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.UpdateVehicleCommand
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase

class UpdateVehicleUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : UpdateVehicleUseCase,
    BaseUseCaseImpl<UpdateVehicleCommand, Unit>() {
    override suspend fun internalExecute(command: UpdateVehicleCommand) {
        val vehicleId = command.vehicle.id ?: throw Exception("Id is required to update vehicle")
        val vehicle = vehiclesRepository.read(vehicleId) ?: throw Exception("Could not find vehicle with id $vehicleId")

        val updated = vehicle.updateVehicle(command.vehicle, command.role)

        vehiclesRepository.update(vehicleId, updated)
    }

    override suspend fun useCaseDescription(): String {
        return "Get vehicles data and update vehicle"
    }
}
