package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.domain.user.security.TokenService
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class UpdateSelfUserHandlerImpl(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val tokenService: TokenService,
) : BaseHTTPHandler<UpdateSelfUserHandlerBody, AuthenticateOutputDto>(),
    UpdateSelfUserHandler {
    override suspend fun handle(body: UpdateSelfUserHandlerBody): HttpResult<AuthenticateOutputDto> {
        val claims = body.claims
        val body = body.user

        if (claims.userId != body.id) {
            throw HandlerException.UnauthenticatedOperation()
        }

        val user = getUserUseCase.execute(body.id).getOrThrow()

        val userUpdate =
            user.updateFull(
                addressId = body.addressId,
                companyId = body.companyId,
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                password = body.password,
                phone = body.phone,
                cpf = body.cpf,
                isActive = true,
            )

        updateUserUseCase.execute(userUpdate).getOrThrow()

        val updatedUser = getUserUseCase.execute(user.id).getOrNull()!!

        val userOutputDto = updatedUser.toOutputDto()
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Accepted)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Client - Update client info"

        configure.requestBody {
            schema = jsonSchema<UpdateSelfUserHandlerBody>()
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
