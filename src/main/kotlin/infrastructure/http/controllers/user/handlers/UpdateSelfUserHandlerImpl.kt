package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class UpdateSelfUserHandlerImpl(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val tokenService: TokenService
) : UpdateSelfUserHandler, BaseHTTPHandler<UpdateSelfUserHandlerBody, AuthenticateOutputDto>() {
    override suspend fun handle(body: UpdateSelfUserHandlerBody): HttpResult<AuthenticateOutputDto> {
        val claims = body.claims
        val body = body.user

        if (claims.userId != body.id) {
            throw HandlerException.UnauthenticatedOperation()
        }

        val user = getUserUseCase.execute(body.id).getOrNull()!!

        val userUpdate = user.updateFull(
            addressId = body.addressId,
            companyId = body.companyId,
            firstName = body.firstName,
            lastName = body.lastName,
            email = body.email,
            password = body.password,
            phone = body.phone,
            cpf = body.cpf,
            isActive = true
        )

        updateUserUseCase.execute(userUpdate)

        val updatedUser = getUserUseCase.execute(user.id).getOrNull()!!

        val userOutputDto = updatedUser.toOutputDto()
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Accepted)
    }
}