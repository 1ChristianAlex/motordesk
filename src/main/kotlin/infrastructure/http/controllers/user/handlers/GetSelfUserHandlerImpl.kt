package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import com.khrix.infrastructure.security.UserClaims
import io.ktor.http.*

class GetSelfUserHandlerImpl(
    private val getUserUseCase: GetUserUseCase
) : GetSelfUserHandler, BaseHTTPHandler<UserClaims, UserOutputDto>() {
    override suspend fun handle(body: UserClaims): HttpResult<UserOutputDto> {
        val user = getUserUseCase.execute(body.userId).getOrThrow()
        return HttpResult(user.toOutputDto(), HttpStatusCode.Accepted)
    }
}