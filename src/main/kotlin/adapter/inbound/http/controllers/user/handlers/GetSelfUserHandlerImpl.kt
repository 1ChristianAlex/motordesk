package com.khrix.adapter.inbound.http.controllers.user.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.adapter.inbound.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.adapter.security.UserClaims
import com.khrix.domain.user.usecase.GetUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class GetSelfUserHandlerImpl(
    private val getUserUseCase: GetUserUseCase,
) : BaseHTTPHandler<UserClaims, UserOutputDto>(),
    GetSelfUserHandler {
    override suspend fun handle(body: UserClaims): HttpResult<UserOutputDto> {
        val user = getUserUseCase.execute(body.userId).getOrThrow()
        return HttpResult(user.toOutputDto(), HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Client - Get self client info"
        configure.description = "Get self client info"

        configure.responses {
            HttpStatusCode.OK {
                schema = jsonSchema<HttpResult<UserOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
