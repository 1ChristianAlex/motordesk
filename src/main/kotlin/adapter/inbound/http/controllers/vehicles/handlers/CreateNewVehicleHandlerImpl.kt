package com.khrix.adapter.inbound.http.controllers.vehicles.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.adapter.inbound.http.controllers.vehicles.resources.dto.VehicleOutputDto
import com.khrix.adapter.inbound.http.controllers.vehicles.resources.mappers.toOutputDto
import com.khrix.domain.vehicle.usecase.CreateNewVehicleUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class CreateNewVehicleHandlerImpl(
    private val createNewVehicleUseCase: CreateNewVehicleUseCase,
) : BaseHTTPHandler<VehicleInputDto, VehicleOutputDto>(),
    CreateNewVehicleHandler {
    override suspend fun handle(body: VehicleInputDto): HttpResult<VehicleOutputDto> {
        val vehicleData = body.toModel()

        val newVehicle = createNewVehicleUseCase.execute(vehicleData).getOrThrow()
        return HttpResult(newVehicle.toOutputDto(), HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Manager - Create a new vehicle"
        configure.description = "Create a new vehicle to be used on a service order"

        configure.requestBody {
            schema = jsonSchema<VehicleInputDto>()
        }
        configure.responses {
            HttpStatusCode.Accepted {
                schema = jsonSchema<HttpResult<VehicleOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
