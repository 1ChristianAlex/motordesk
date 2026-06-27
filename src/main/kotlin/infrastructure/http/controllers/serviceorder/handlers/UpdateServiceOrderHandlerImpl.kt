package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers.toOutputDto
import io.ktor.http.*

class UpdateServiceOrderHandlerImpl(
    private val updateServiceOrderUseCase: UpdateServiceOrderUseCase,
    private val getServiceOrdersByCodeUseCase: GetServiceOrdersByCodeUseCase
) : UpdateServiceOrderHandler,
    BaseHTTPHandler<UpdateServiceOrderInputDto, ServiceOrderOutputDto>() {
    override suspend fun handle(body: UpdateServiceOrderInputDto): HttpResult<ServiceOrderOutputDto> {
        updateServiceOrderUseCase.execute(body.toCommand()).getOrThrow()
        val servicerOrder = getServiceOrdersByCodeUseCase.execute(body.code).getOrThrow()

        return HttpResult(servicerOrder.toOutputDto(), HttpStatusCode.OK)
    }
}