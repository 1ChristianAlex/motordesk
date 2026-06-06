package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.core.HTTPHandler

interface GetVehicleByOwnerHandler : HTTPHandler<Int, List<VehicleOutputDto>>