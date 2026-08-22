package com.khrix.adapter.http.controllers.vehicles.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleOutputDto

interface CreateNewVehicleHandler : HTTPHandler<VehicleInputDto, VehicleOutputDto>
