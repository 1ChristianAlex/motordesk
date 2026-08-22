package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class UpdateServiceOrderHandlerImpl(
    private val updateServiceOrderUseCase: UpdateServiceOrderUseCase,
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase,
) : BaseHTTPHandler<UpdateServiceOrderInputDto, ServiceOrderWithHistoryOutputDto>(),
    UpdateServiceOrderHandler {
    override suspend fun handle(body: UpdateServiceOrderInputDto): HttpResult<ServiceOrderWithHistoryOutputDto> {
        updateServiceOrderUseCase.execute(body.toCommand()).getOrThrow()
        val servicerOrder = getServiceOrdersByCodeUseCase.execute(body.code).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Manager - Update service order"
        configure.description = "Update service order"

        configure.requestBody {
            schema = jsonSchema<UpdateServiceOrderInputDto>()
        }
        configure.responses {
            HttpStatusCode.OK {
                schema = jsonSchema<HttpResult<ServiceOrderWithHistoryOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
