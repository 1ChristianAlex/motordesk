package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

class CreateNewUserHandlerImpl(
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val verifyIsEmailAvailableUseCase: VerifyIsEmailAvailableUseCase,
    private val tokenService: TokenService
) : CreateNewUserHandler {
    override suspend fun handler(body: ClientRegisterDto): HttpResult<AuthenticateOutputDto> {
        return try {
            val userModel = body.user.toDomain()
            val isEmailAvailable = verifyIsEmailAvailableUseCase.execute(userModel.email).getOrElse { false }

            if (!isEmailAvailable) {
                throw Exception("Email is not available")
            }

            val user = createNewUserUseCase.execute(
                CreateNewUserUseCaseCommand(
                    user = userModel,
                    address = body.address.toDomain(),
                    company = body.company?.toDomain()
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