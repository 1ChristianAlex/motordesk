package com.khrix.adapter.inbound.http.controllers.serviceorder.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.mappers.toOutputDto
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrdersByClientIdUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class GetClientServicesOrderHandlerImpl(
    private val getServiceOrdersByClientIdUseCase: GetServiceOrdersByClientIdUseCase,
) : BaseHTTPHandler<Int, List<ServiceOrderOutputDto>>(),
    GetClientServicesOrderHandler {
    override suspend fun handle(body: Int): HttpResult<List<ServiceOrderOutputDto>> {
        val servicerOrder =
            getServiceOrdersByClientIdUseCase
                .execute(
                    body,
                ).getOrThrow()

        return HttpResult(servicerOrder.map { it.toOutputDto() }, HttpStatusCode.OK)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Client - Get services orders"
        configure.description = "Get all service order from self"

        configure.responses {
            HttpStatusCode.OK {
                schema = jsonSchema<HttpResult<List<ServiceOrderOutputDto>>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
