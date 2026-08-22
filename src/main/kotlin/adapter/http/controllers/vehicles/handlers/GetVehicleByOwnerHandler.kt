package com.khrix.adapter.http.controllers.vehicles.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleOutputDto

interface GetVehicleByOwnerHandler : HTTPHandler<Int, List<VehicleOutputDto>>
