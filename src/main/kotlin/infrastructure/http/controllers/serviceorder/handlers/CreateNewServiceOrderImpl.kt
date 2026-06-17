package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.CreateServiceOrderUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*

class CreateNewServiceOrderImpl(
    private val createNewServiceOrderUseCase: CreateServiceOrderUseCase
) : CreateNewServiceOrder,
    BaseHTTPHandler<ServiceOrderInputDto, ServiceOrderOutputDto>() {
    override suspend fun handle(body: ServiceOrderInputDto): HttpResult<ServiceOrderOutputDto> {
        val servicerOrder = createNewServiceOrderUseCase.execute(body.toCommand()).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.Created)
    }
}