package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*

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
}