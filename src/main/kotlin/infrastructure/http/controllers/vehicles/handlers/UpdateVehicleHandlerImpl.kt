package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleUpdateInputDto
import io.ktor.http.*

class UpdateVehicleHandlerImpl(
    private val updateVehicleUseCase: UpdateVehicleUseCase,
    private val getVehicleByIdUseCase: GetVehicleByIdUseCase
) : UpdateVehicleHandler, BaseHTTPHandler<VehicleUpdateInputDto, Unit>() {
    override suspend fun handle(body: VehicleUpdateInputDto): HttpResult<Unit> {
        val vehicle = getVehicleByIdUseCase.execute(body.id).getOrThrow()

        updateVehicleUseCase.execute(vehicle.copy(color = body.color ?: "", mileage = body.mileage ?: 0)).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }
}