package com.khrix.adapter.inbound.http.controllers.login

import com.khrix.adapter.inbound.http.controllers.core.AppController
import com.khrix.adapter.inbound.http.controllers.core.getBody
import com.khrix.adapter.inbound.http.controllers.login.handlers.LoginHandler
import com.khrix.adapter.inbound.http.controllers.login.resources.LoginResource
import com.khrix.adapter.inbound.http.controllers.login.resources.dto.LoginInputDto
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import io.ktor.server.routing.openapi.describe

class LoginController(
    private val loginHandler: LoginHandler,
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
