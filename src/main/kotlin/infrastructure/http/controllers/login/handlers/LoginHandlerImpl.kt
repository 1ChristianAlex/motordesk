package com.khrix.infrastructure.http.controllers.login.handlers

import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.security.TokenService
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginInputDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import io.ktor.http.*
import io.ktor.openapi.*

class LoginHandlerImpl(
    private val loginUserUseCase: LoginUserUseCase,
    private val tokenService: TokenService
) : LoginHandler, BaseHTTPHandler<LoginInputDto, AuthenticateOutputDto>() {
    override suspend fun handle(body: LoginInputDto): HttpResult<AuthenticateOutputDto> {
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

    override fun description(
        configure: Operation.Builder,
    ) {
        configure.summary = "Login"
        configure.description = "You can login using email, cpf or cnpj"
        configure.requestBody {
            schema = jsonSchema<LoginInputDto>()
        }
        configure.responses {
            HttpStatusCode.Accepted {
                schema = jsonSchema<HttpResult<AuthenticateOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}