package com.khrix.adapter.http.controllers.register

import com.khrix.adapter.http.controllers.core.AppController
import com.khrix.adapter.http.controllers.core.getBody
import com.khrix.adapter.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.adapter.http.controllers.register.handlers.CreateNewUserRequest
import com.khrix.adapter.http.controllers.register.resources.RegisterResource
import com.khrix.adapter.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.domain.user.model.Role
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import io.ktor.server.routing.openapi.describe

class RegisterController(
    private val createNewUserHandler: CreateNewUserHandler,
) : AppController() {
    override fun map(routing: Routing) {
        with(routing) {
            post<RegisterResource.Client> {
                val body = getBody<ClientRegisterDto>()
                call.send(createNewUserHandler.handler(CreateNewUserRequest(body)))
            }.describe(createNewUserHandler::description)
            manager {
                post<RegisterResource.Manager> {
                    val body = getBody<ClientRegisterDto>()
                    val request = CreateNewUserRequest(body)
                    request.updateRole(Role.MANAGER)
                    call.send(createNewUserHandler.handler(request))
                }.describe(createNewUserHandler::description)
            }
        }
    }
}
