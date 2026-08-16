package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase

class UpdateVehicleUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository,
) : BaseUseCaseImpl<Vehicle, Unit>(),
    UpdateVehicleUseCase {
    override suspend fun internalExecute(command: Vehicle) {
        val vehicleId = command.id
        val vehicle = vehiclesRepository.read(vehicleId) ?: throw Exception("Could not find vehicle with id $vehicleId")

        val updated =
            vehicle
                .updateColor(command.color)
                .updateMileage(command.mileage)

        vehiclesRepository.update(vehicleId, updated)
    }

    override suspend fun useCaseDescription(): String = "Get vehicles data and update vehicle"
}
