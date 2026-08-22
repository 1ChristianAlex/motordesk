package com.khrix.adapter.http.controllers.vehicles.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleUpdateInputDto

interface UpdateVehicleHandler : HTTPHandler<VehicleUpdateInputDto, Unit>
