package com.khrix.domain.vehicle.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.vehicle.model.Vehicle

interface GetVehicleByOwnerIdUseCase : BaseUseCase<Int, List<Vehicle>>
