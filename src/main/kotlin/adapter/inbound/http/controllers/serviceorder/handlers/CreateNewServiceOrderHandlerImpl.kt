package com.khrix.adapter.inbound.http.controllers.serviceorder.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.mappers.toOutputDto
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class CreateNewServiceOrderHandlerImpl(
    private val createNewServiceOrderUseCase: CreateServiceOrderUseCase,
) : BaseHTTPHandler<ServiceOrderInputDto, ServiceOrderOutputDto>(),
    CreateNewServiceOrderHandler {
    override suspend fun handle(body: ServiceOrderInputDto): HttpResult<ServiceOrderOutputDto> {
        val servicerOrder = createNewServiceOrderUseCase.execute(body.toCommand()).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.Created)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Create new service order"
        configure.description = "Only manager - Create new service order to a given client"
        configure.requestBody {
            schema = jsonSchema<ServiceOrderInputDto>()
        }
        configure.responses {
            HttpStatusCode.Created {
                schema = jsonSchema<HttpResult<ServiceOrderOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
