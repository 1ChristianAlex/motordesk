package com.khrix.infrastructure.http.controllers.register

import com.khrix.domain.user.model.Role
import com.khrix.infrastructure.http.controllers.core.AuthNames
import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserRequest
import com.khrix.infrastructure.http.controllers.register.resources.RegisterResource
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.core.AppController
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class RegisterController(
    private val createNewUserHandler: CreateNewUserHandler
) : AppController() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            post<RegisterResource> {
                val body = getBody<ClientRegisterDto>()
                call.send(createNewUserHandler.handler(CreateNewUserRequest(body)))
            }

            authenticate(AuthNames.AUTH_JWT_MANAGER) {
                post<RegisterResource> {
                    val body = getBody<ClientRegisterDto>()
                    val request = CreateNewUserRequest(body)
                    request.updateRole(Role.MANAGER)
                    call.send(createNewUserHandler.handler(request))
                }
            }
        }
    }
}
