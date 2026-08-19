package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto

interface CreateNewVehicleHandler : HTTPHandler<VehicleInputDto, VehicleOutputDto>
