package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.user.model.Role
import com.khrix.domain.user.security.TokenService
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCaseCommand
import com.khrix.infrastructure.http.controllers.core.BaseHTTPHandler
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import io.ktor.http.*

class CreateNewUserHandlerImpl(
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val verifyIsUserDataAvailableUseCase: VerifyIsUserDataAvailableUseCase,
    private val tokenService: TokenService
) : CreateNewUserHandler, BaseHTTPHandler<CreateNewUserRequest, AuthenticateOutputDto>() {
    override suspend fun handle(body: CreateNewUserRequest): HttpResult<AuthenticateOutputDto> {
        val userModel = body.clientRegisterDto.user.toDomain()
        val addressModel = body.clientRegisterDto.address.toDomain()
        val companyModel = body.clientRegisterDto.company?.toDomain()

        verifyIsUserDataAvailableUseCase.execute(
            VerifyIsUserDataAvailableUseCaseCommand(
                email = userModel.email,
                cpf = userModel.cpf
            )
        ).getOrThrow()

        val user = createNewUserUseCase.execute(
            CreateNewUserUseCaseCommand(
                user = userModel, address = addressModel, company = companyModel
            )
        ).getOrThrow()

        val userOutputDto = user.run {
            toOutputDto(this.role == Role.CLIENT)
        }
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Created)
    }
}