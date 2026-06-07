package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.CreateNewVehicleUseCase
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import io.ktor.http.*

class CreateNewVehicleHandlerImpl(
    private val createNewVehicleUseCase: CreateNewVehicleUseCase
) : CreateNewVehicleHandler, BaseHTTPHandler<VehicleInputDto, VehicleOutputDto>() {
    override suspend fun handle(body: VehicleInputDto): HttpResult<VehicleOutputDto> {
        val vehicleData = body.toModel()

        val newVehicle = createNewVehicleUseCase.execute(vehicleData).getOrThrow()
        return HttpResult(newVehicle.toOutputDto(), HttpStatusCode.Accepted)
    }
}