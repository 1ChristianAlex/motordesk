package com.khrix.infrastructure.http.controllers.login

import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.resources.LoginResource
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginDto
import com.khrix.infrastructure.http.core.AppController
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class LoginController(
    private val loginHandler: LoginHandler
) : AppController {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            post<LoginResource> {
                val body = getBody<LoginDto>()
                call.respond(loginHandler.handler(body))
            }
        }
    }
}