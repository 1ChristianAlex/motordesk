package com.khrix.infrastructure.http.controllers.login.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class LoginHandlerImpl(
    private val loginUserUseCase: LoginUserUseCase,
    private val tokenService: TokenService
) : LoginHandler {
    override suspend fun handler(body: LoginDto): HttpResult<AuthenticateOutputDto> {
        return try {
            val user = loginUserUseCase.execute(
                LoginTypes.create(
                    userName = body.userName,
                    password = body.password
                )
            ).getOrThrow()

            val userOutputDto = user.toOutputDto()
            val token = tokenService.generate(user)

            HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Created)
        } catch (error: ValidationErrorResult) {
            HttpResult(null, HttpStatusCode.BadRequest, error.validationErrors)
        } catch (exception: Exception) {
            HttpResult(
                null,
                HttpStatusCode.BadRequest,
                listOf(exception.message ?: "An error occurred")
            )
        }
    }
}