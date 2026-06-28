package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.GetVehicleByOwnerIdUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.mappers.toOutputDto
import io.ktor.http.*
import io.ktor.openapi.*

class GetVehicleByOwnerHandlerImpl(
    private val getVehicleByOwnerIdUseCase: GetVehicleByOwnerIdUseCase
) : GetVehicleByOwnerHandler, BaseHTTPHandler<Int, List<VehicleOutputDto>>() {
    override suspend fun handle(body: Int): HttpResult<List<VehicleOutputDto>> {
        val vehicles = getVehicleByOwnerIdUseCase.execute(body).getOrThrow()

        return HttpResult(vehicles.map { it.toOutputDto() }, HttpStatusCode.Accepted)
    }

    override fun description(
        configure: Operation.Builder,
    ) {
        configure.summary = "Manager - Get vehicle by owner"
        configure.description = "Get vehicle given a client data"

        configure.responses {
            HttpStatusCode.Accepted {
                schema = jsonSchema<HttpResult<List<VehicleOutputDto>>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}