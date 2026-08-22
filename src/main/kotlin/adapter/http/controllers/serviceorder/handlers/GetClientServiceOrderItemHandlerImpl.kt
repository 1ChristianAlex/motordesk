package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.http.controllers.core.HttpResult
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.adapter.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.adapter.http.controllers.serviceorder.resources.mappers.toOutputDto
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class GetClientServiceOrderItemHandlerImpl(
    private val getClientServiceOrdersByCodeUseCase: GetClientServiceOrdersByCodeUseCase,
) : BaseHTTPHandler<ClientServiceOrderItemInputDto, ServiceOrderWithHistoryOutputDto>(),
    GetClientServiceOrderItemHandler {
    override suspend fun handle(body: ClientServiceOrderItemInputDto): HttpResult<ServiceOrderWithHistoryOutputDto> {
        val servicerOrder = getClientServiceOrdersByCodeUseCase.execute(body.toCommand()).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Client - Get service order"
        configure.description = "Client can only retrieve its own service order given a service order's code"

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
