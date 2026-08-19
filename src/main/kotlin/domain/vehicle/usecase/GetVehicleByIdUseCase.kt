package com.khrix.domain.vehicle.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.vehicle.model.Vehicle

interface GetVehicleByIdUseCase : BaseUseCase<Int, Vehicle>
