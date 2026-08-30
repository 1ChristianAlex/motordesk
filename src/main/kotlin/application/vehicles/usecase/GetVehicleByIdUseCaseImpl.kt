package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.port.repository.VehiclesRepository
import com.khrix.domain.vehicle.port.usecase.GetVehicleByIdUseCase

class GetVehicleByIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository,
) : BaseUseCaseImpl<Int, Vehicle>(),
    GetVehicleByIdUseCase {
    override suspend fun internalExecute(command: Int): Vehicle =
        vehiclesRepository.read(command)
            ?: throw NoSuchElementException("Vehicle with id $command does not exist")

    override suspend fun useCaseDescription(): String = "Get vehicle by id"
}
