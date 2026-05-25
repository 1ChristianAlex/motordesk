package com.khrix.infrastructure.http.controllers.login.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class LoginHandlerImpl(
    private val loginUserUseCase: LoginUserUseCase,
    private val tokenService: TokenService
) : LoginHandler, BaseHTTPHandler<LoginDto, AuthenticateOutputDto>() {
    override suspend fun handle(body: LoginDto): HttpResult<AuthenticateOutputDto> {
        val user = loginUserUseCase.execute(
            LoginTypes.create(
                userName = body.userName,
                password = body.password
            )
        ).getOrThrow()

        val userOutputDto = user.toOutputDto()
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Accepted)
    }
}