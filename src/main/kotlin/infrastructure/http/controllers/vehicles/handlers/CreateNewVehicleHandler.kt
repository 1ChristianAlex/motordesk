package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.core.HTTPHandler
import com.khrix.infrastructure.security.UserClaims

data class NewVehicleRequest(val claims: UserClaims, val vehicle: VehicleInputDto)

interface CreateNewVehicleHandler : HTTPHandler<NewVehicleRequest, VehicleOutputDto>