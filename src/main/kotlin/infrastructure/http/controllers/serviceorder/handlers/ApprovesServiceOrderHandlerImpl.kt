package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.ApprovesServiceOrderUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ApprovesServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class ApprovesServiceOrderHandlerImpl(
    private val approvesServiceOrderUseCase: ApprovesServiceOrderUseCase,
) : BaseHTTPHandler<ApprovesServiceOrderInputDto, Unit>(),
    ApprovesServiceOrderHandler {
    override suspend fun handle(body: ApprovesServiceOrderInputDto): HttpResult<Unit> {
        approvesServiceOrderUseCase.execute(body.toCommand())

        return HttpResult(null, HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Client - Approves service order"
        configure.description = "Approves service order given a token"

        configure.requestBody {
            schema = jsonSchema<ApprovesServiceOrderInputDto>()
        }
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
