package com.khrix.application.vehicles.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.port.repository.VehiclesRepository
import com.khrix.domain.vehicle.port.usecase.GetVehicleByOwnerIdUseCase

class GetVehicleByOwnerIdUseCaseImpl(
    private val vehiclesRepository: VehiclesRepository,
) : BaseUseCaseImpl<Int, List<Vehicle>>(),
    GetVehicleByOwnerIdUseCase {
    override suspend fun internalExecute(command: Int): List<Vehicle> = vehiclesRepository.getVehicleByOwnerId(command)

    override suspend fun useCaseDescription(): String = "Get vehicle by id"
}
