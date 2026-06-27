package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByClientIdUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*

class GetClientServicesOrderHandlerImpl(
    private val getServiceOrdersByClientIdUseCase: GetServiceOrdersByClientIdUseCase
) : GetClientServicesOrderHandler,
    BaseHTTPHandler<Int, List<ServiceOrderOutputDto>>() {
    override suspend fun handle(body: Int): HttpResult<List<ServiceOrderOutputDto>> {
        val servicerOrder = getServiceOrdersByClientIdUseCase.execute(
            body
        ).getOrThrow()

        return HttpResult(servicerOrder.map { it.toOutputDto() }, HttpStatusCode.OK)
    }
}