package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*
import io.ktor.openapi.*

class GetClientServiceOrderItemHandlerImpl(
    private val getClientServiceOrdersByCodeUseCase: GetClientServiceOrdersByCodeUseCase
) : GetClientServiceOrderItemHandler,
    BaseHTTPHandler<ClientServiceOrderItemInputDto, ServiceOrderOutputDto>() {
    override suspend fun handle(body: ClientServiceOrderItemInputDto): HttpResult<ServiceOrderOutputDto> {
        val servicerOrder = getClientServiceOrdersByCodeUseCase.execute(body.toCommand()).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }

    override fun description(
        configure: Operation.Builder,
    ) {
        configure.summary = "Client - Get service order"
        configure.description = "Client can only retrieve its own service order given a service order's code"

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