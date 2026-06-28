package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*
import io.ktor.openapi.*

class GetServiceOrderItemHandlerImpl(
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase
) : GetServiceOrderItemHandler,
    BaseHTTPHandler<String, ServiceOrderOutputDto>() {
    override suspend fun handle(body: String): HttpResult<ServiceOrderOutputDto> {
        val servicerOrder = getServiceOrdersByCodeUseCase.execute(
            body
        ).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }

    override fun description(
        configure: Operation.Builder,
    ) {
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