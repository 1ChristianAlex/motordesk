package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.model.CreateNewVehicleUseCase
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class CreateNewVehicleHandlerImpl(
    private val createNewVehicleUseCase: CreateNewVehicleUseCase
) : CreateNewVehicleHandler, BaseHTTPHandler<NewVehicleRequest, VehicleOutputDto>() {
    override suspend fun handle(body: NewVehicleRequest): HttpResult<VehicleOutputDto> {
        val vehicleData = body.run {
            vehicle.toModel(claims.userId)
        }

        val newVehicle = createNewVehicleUseCase.execute(vehicleData).getOrThrow()
        return HttpResult(newVehicle.toOutputDto(), HttpStatusCode.Accepted)
    }
}