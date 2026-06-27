package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*

class GetClientServiceOrderItemHandlerImpl(
    private val getClientServiceOrdersByCodeUseCase: GetClientServiceOrdersByCodeUseCase
) : GetClientServiceOrderItemHandler,
    BaseHTTPHandler<ClientServiceOrderItemInputDto, ServiceOrderOutputDto>() {
    override suspend fun handle(body: ClientServiceOrderItemInputDto): HttpResult<ServiceOrderOutputDto> {
        val servicerOrder = getClientServiceOrdersByCodeUseCase.execute(body.toCommand()).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }
}