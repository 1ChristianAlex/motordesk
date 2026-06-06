package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.GetVehicleByOwnerIdUseCase
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class GetVehicleByOwnerHandlerImpl(
    private val getVehicleByOwnerIdUseCase: GetVehicleByOwnerIdUseCase
) : GetVehicleByOwnerHandler, BaseHTTPHandler<Int, List<VehicleOutputDto>>() {
    override suspend fun handle(body: Int): HttpResult<List<VehicleOutputDto>> {
        val vehicles = getVehicleByOwnerIdUseCase.execute(body).getOrThrow()

        return HttpResult(vehicles.map { it.toOutputDto() }, HttpStatusCode.Accepted)
    }
}