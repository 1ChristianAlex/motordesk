package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import io.ktor.http.*

class UpdateVehicleHandlerImpl(
    private val updateVehicleUseCase: UpdateVehicleUseCase
) : UpdateVehicleHandler, BaseHTTPHandler<VehicleInputDto, Unit>() {
    override suspend fun handle(body: VehicleInputDto): HttpResult<Unit> {

        updateVehicleUseCase.execute(body.toModel()).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }
}