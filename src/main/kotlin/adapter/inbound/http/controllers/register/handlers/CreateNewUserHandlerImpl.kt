package com.khrix.adapter.inbound.http.controllers.register.handlers

import com.khrix.adapter.inbound.http.controllers.core.BaseHTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.HttpResult
import com.khrix.adapter.inbound.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.inbound.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.adapter.inbound.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.security.TokenService
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCaseCommand
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.Operation
import io.ktor.openapi.jsonSchema

class CreateNewUserHandlerImpl(
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val verifyIsUserDataAvailableUseCase: VerifyIsUserDataAvailableUseCase,
    private val tokenService: TokenService,
) : BaseHTTPHandler<CreateNewUserRequest, AuthenticateOutputDto>(),
    CreateNewUserHandler {
    override suspend fun handle(body: CreateNewUserRequest): HttpResult<AuthenticateOutputDto> {
        val userModel = body.clientRegisterDto.user.toDomain()
        val addressModel = body.clientRegisterDto.address.toDomain()
        val companyModel = body.clientRegisterDto.company?.toDomain()

        verifyIsUserDataAvailableUseCase
            .execute(
                VerifyIsUserDataAvailableUseCaseCommand(
                    email = userModel.email,
                    cpf = userModel.cpf,
                ),
            ).getOrThrow()

        val user =
            createNewUserUseCase
                .execute(
                    CreateNewUserUseCaseCommand(
                        user = userModel,
                        address = addressModel,
                        company = companyModel,
                    ),
                ).getOrThrow()

        val userOutputDto =
            user.run {
                toOutputDto(this.role == Role.CLIENT)
            }
        val token = tokenService.generate(user)

        return HttpResult(AuthenticateOutputDto(token, userOutputDto), HttpStatusCode.Created)
    }

    override fun description(configure: Operation.Builder) {
        configure.summary = "Create new user"
        configure.description =
            "Client can create a new account - You can only create a high role account if you are manager or ADM"
        configure.requestBody {
            schema = jsonSchema<ClientRegisterDto>()
        }
        configure.responses {
            HttpStatusCode.Created {
                schema = jsonSchema<HttpResult<AuthenticateOutputDto>>()
            }
            HttpStatusCode.BadRequest {
                schema = jsonSchema<HttpResult<Nothing>>()
            }
        }
    }
}
