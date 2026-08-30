package com.khrix.adapter.inbound.http.controllers.user.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.inbound.http.controllers.core.exceptions.HandlerException
import com.khrix.adapter.inbound.http.controllers.user.resources.dto.UserInputDto
import com.khrix.adapter.inbound.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.domain.user.port.security.TokenService
import com.khrix.domain.user.port.usecase.GetUserUseCase
import com.khrix.domain.user.port.usecase.UpdateUserUseCase
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
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
            user.copy(
                addressId = body.addressId ?: user.addressId,
                companyId = body.companyId ?: user.companyId,
                firstName = Name(body.firstName ?: user.firstName.value),
                lastName = Name(body.lastName ?: user.lastName.value),
                email = Email(body.email ?: user.email.value),
                password = Password.Raw(body.password ?: user.password.value),
                phone = Phone(body.phone ?: user.phone.value),
                cpf = CPF(body.cpf ?: user.cpf.value),
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
            schema = jsonSchema<UserInputDto>()
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
