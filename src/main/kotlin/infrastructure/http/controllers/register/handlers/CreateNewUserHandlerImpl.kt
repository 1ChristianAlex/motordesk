package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.BaseHTTPHandler
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class CreateNewUserHandlerImpl(
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val verifyIsEmailAvailableUseCase: VerifyIsEmailAvailableUseCase,
    private val tokenService: TokenService
) : CreateNewUserHandler, BaseHTTPHandler<ClientRegisterDto, AuthenticateOutputDto>() {
    override suspend fun handle(body: ClientRegisterDto): HttpResult<AuthenticateOutputDto> {
        val userModel = body.user.toDomain()
        val isEmailAvailable = verifyIsEmailAvailableUseCase.execute(userModel.email).getOrElse { false }

        if (!isEmailAvailable) {
            throw Exception("Email is not available")
        }

        val user = createNewUserUseCase.execute(
            CreateNewUserUseCaseCommand(
                user = userModel, address = body.address.toDomain(), company = body.company?.toDomain()
            )
        ).getOrThrow()

        val userOutputDto = user.toOutputDto()
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Created)
    }
}