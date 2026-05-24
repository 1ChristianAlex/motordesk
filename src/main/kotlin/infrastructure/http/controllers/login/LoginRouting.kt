package com.khrix.infrastructure.http.controllers.login

import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.resources.LoginResource
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginDto
import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class LoginRouting(
    private val loginHandler: LoginHandler
) : AppRouting {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            post<LoginResource> {
                try {
                    val body = call.receive<LoginDto>()
                    call.respond(loginHandler.handler(body))
                } catch (exception: Exception) {
                    call.respond(
                        HttpResult(
                            null,
                            HttpStatusCode.BadRequest,
                            listOf(exception.message ?: "An error occurred")
                        )
                    )
                }

            }
        }
    }
}