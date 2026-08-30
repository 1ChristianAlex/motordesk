package com.khrix.adapter.inbound.http.controllers.vehicles.handlers

import com.khrix.adapter.inbound.http.controllers.core.HTTPHandler
import com.khrix.adapter.inbound.http.controllers.vehicles.resources.dto.VehicleOutputDto

interface GetVehicleByOwnerHandler : HTTPHandler<Int, List<VehicleOutputDto>>
