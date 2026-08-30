package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.domain.vehicle.usecase.GetVehicleByOwnerIdUseCase

class GetVehicleByOwnerIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository
) : GetVehicleByOwnerIdUseCase,
    BaseUseCaseImpl<Int, List<Vehicle>>() {
    override suspend fun internalExecute(command: Int): List<Vehicle> {
        return vehiclesRepository.getVehicleByOwnerId(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Get vehicle by id"
    }
}
