package com.khrix.domain.vehicle.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.Role
import com.khrix.domain.vehicle.model.Vehicle

data class UpdateVehicleCommand(val vehicle: Vehicle, val role: Role)

interface UpdateVehicleUseCase : BaseUseCase<UpdateVehicleCommand, Unit>