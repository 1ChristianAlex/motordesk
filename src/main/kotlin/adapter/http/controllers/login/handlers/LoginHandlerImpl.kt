package com.khrix.adapter.http.controllers.login.handlers

import com.khrix.adapter.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.http.controllers.core.HttpResult
import com.khrix.adapter.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.http.controllers.login.resources.dto.LoginInputDto
import com.khrix.adapter.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.security.TokenService
import com.khrix.domain.user.usecase.LoginUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class LoginHandlerImpl(
    private val loginUserUseCase: LoginUserUseCase,
    private val tokenService: TokenService,
) : BaseHTTPHandler<LoginInputDto, AuthenticateOutputDto>(),
    LoginHandler {
    override suspend fun handle(body: LoginInputDto): HttpResult<AuthenticateOutputDto> {
        val user =
            loginUserUseCase
                .execute(
                    LoginTypes.create(
                        userName = body.userName,
                        password = body.password,
                    ),
                ).getOrThrow()

        val userOutputDto = user.toOutputDto()
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
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
