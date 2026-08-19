package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto

interface GetVehicleByOwnerHandler : HTTPHandler<Int, List<VehicleOutputDto>>
