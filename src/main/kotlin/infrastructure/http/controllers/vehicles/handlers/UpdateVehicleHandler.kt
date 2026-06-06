package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.core.HTTPHandler

data class UpdateVehicleRequest(val vehicle: VehicleInputDto, val userId: Int)

interface UpdateVehicleHandler : HTTPHandler<UpdateVehicleRequest, Unit>