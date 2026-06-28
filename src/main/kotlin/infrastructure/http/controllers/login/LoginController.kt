package com.khrix.infrastructure.http.controllers.login

import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.resources.LoginResource
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginInputDto
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.*

class LoginController(
    private val loginHandler: LoginHandler
) : AppController() {
    override fun map(routing: Routing) {
        with(routing) {
            post<LoginResource> {
                val body = getBody<LoginInputDto>()
                call.send(loginHandler.handler(body))
            }.describe(loginHandler::description)
        }
    }
}