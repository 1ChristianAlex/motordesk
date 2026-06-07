package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.controllers.core.HTTPHandler


interface UpdateVehicleHandler : HTTPHandler<VehicleInputDto, Unit>