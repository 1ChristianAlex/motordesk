package com.khrix.infrastructure.http.controllers.vehicles.handlers

import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import io.ktor.http.*

class DeleteVehicleHandlerImpl(
    private val deleteVehicleByIdUseCase: DeleteVehicleByIdUseCase
) : DeleteVehicleHandler, BaseHTTPHandler<Int, Unit>() {
    override suspend fun handle(body: Int): HttpResult<Unit> {
        deleteVehicleByIdUseCase.execute(body).getOrThrow()

        return HttpResult(null, HttpStatusCode.Accepted)
    }
}