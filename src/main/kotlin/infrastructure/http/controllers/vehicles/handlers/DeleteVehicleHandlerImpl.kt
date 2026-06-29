package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class DeleteVehicleHandlerImpl(
    private val deleteVehicleByIdUseCase: DeleteVehicleByIdUseCase,
) : BaseHTTPHandler<Int, Unit>(),
    DeleteVehicleHandler {
    override suspend fun handle(body: Int): HttpResult<Unit> {
        deleteVehicleByIdUseCase.execute(body).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Manager - Delete vehicle"
        configure.description = "Virtually delete a vehicle"

        configure.responses {
            HttpStatusCode.Accepted {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
