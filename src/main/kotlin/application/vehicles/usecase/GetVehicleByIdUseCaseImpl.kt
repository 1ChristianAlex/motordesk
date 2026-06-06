package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase

class GetVehicleByIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : GetVehicleByIdUseCase,
    BaseUseCaseImpl<Int, Vehicle>() {
    override suspend fun internalExecute(command: Int): Vehicle {
        return vehiclesRepository.read(command) ?: throw Exception("Vehicle with id $command does not exist")
    }

    override suspend fun useCaseDescription(): String {
        return "Get vehicle by id"
    }
}
