package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.core.HTTPHandler
import com.khrix.infrastructure.http.core.HTTPResult
import io.ktor.http.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CreateNewUserHandler constructor(
    private val createNewUserUseCase: CreateNewUserUseCase
) : HTTPHandler<ClientRegisterDto, UserOutputDto> {
    override suspend fun handler(body: ClientRegisterDto): HTTPResult<UserOutputDto> {
        val result = createNewUserUseCase.execute(
            CreateNewUserUseCaseCommand(
                user = body.user.toDomain(),
                address = body.address.toDomain(),
                cnpj = body.cnpj,
                companyName = body.companyName,
            )
        )
        return suspendCancellableCoroutine {
            result.fold(
                onSuccess = { user ->
                    val outputDto = user.toOutputDto()
                    it.resume(HTTPResult(outputDto, HttpStatusCode.Created))
                },
                onFailure = { error ->
                    it.resume(HTTPResult(null, HttpStatusCode.BadRequest, error.message ?: "An error occurred"))
                }
            )
        }
    }
}