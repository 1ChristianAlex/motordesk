package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleUpdateInputDto

interface UpdateVehicleHandler : HTTPHandler<VehicleUpdateInputDto, Unit>
