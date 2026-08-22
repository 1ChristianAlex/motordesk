package com.khrix.adapter.inbound.http.controllers.serviceorder.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.mappers.toOutputDto
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class GetServiceOrderItemHandlerImpl(
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase,
) : BaseHTTPHandler<String, ServiceOrderWithHistoryOutputDto>(),
    GetServiceOrderItemHandler {
    override suspend fun handle(body: String): HttpResult<ServiceOrderWithHistoryOutputDto> {
        val servicerOrder =
            getServiceOrdersByCodeUseCase
                .execute(
                    body,
                ).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Manager - Get a service order item"
        configure.description = "Get any service order from any client"

        configure.responses {
            HttpStatusCode.OK {
                schema = jsonSchema<HttpResult<ServiceOrderOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
