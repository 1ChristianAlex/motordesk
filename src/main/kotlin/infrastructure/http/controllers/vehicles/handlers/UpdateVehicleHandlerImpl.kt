package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class UpdateVehicleHandlerImpl(
    private val updateVehicleUseCase: UpdateVehicleUseCase
) : UpdateVehicleHandler, BaseHTTPHandler<UpdateVehicleRequest, Unit>() {
    override suspend fun handle(body: UpdateVehicleRequest): HttpResult<Unit> {

        updateVehicleUseCase.execute(body.vehicle.toModel(body.userId)).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }
}