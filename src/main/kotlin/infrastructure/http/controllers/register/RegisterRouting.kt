package com.khrix.infrastructure.http.controllers.register

import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.resources.RegisterResource
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class RegisterRouting(
    private val createNewUserHandler: CreateNewUserHandler
) : AppRouting {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            post<RegisterResource> {
                try {
                    val body = call.receive<ClientRegisterDto>()
                    call.respond(createNewUserHandler.handler(body))
                } catch (exception: Exception) {
                    call.respond(
                        HttpResult(
                            null,
                            HttpStatusCode.BadRequest,
                            listOf(exception.message ?: "An error occurred")
                        )
                    )
                }
                get("/") {
                    call.respond("Hello from register route")
                }
            }
        }
    }
}
