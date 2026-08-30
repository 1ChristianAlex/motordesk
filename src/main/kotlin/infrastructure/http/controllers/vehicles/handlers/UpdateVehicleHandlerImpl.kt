package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleUpdateInputDto
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class UpdateVehicleHandlerImpl(
    private val updateVehicleUseCase: UpdateVehicleUseCase,
    private val getVehicleByIdUseCase: GetVehicleByIdUseCase,
) : BaseHTTPHandler<VehicleUpdateInputDto, Unit>(),
    UpdateVehicleHandler {
    override suspend fun handle(body: VehicleUpdateInputDto): HttpResult<Unit> {
        val vehicle = getVehicleByIdUseCase.execute(body.id).getOrThrow()

        updateVehicleUseCase.execute(vehicle.copy(color = body.color ?: "", mileage = body.mileage ?: 0)).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Manager - Update vehicle"

        configure.requestBody {
            schema = jsonSchema<VehicleUpdateInputDto>()
        }
        configure.responses {
            HttpStatusCode.OK {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
