package com.khrix.infrastructure.http.controllers.user

import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandlerBody
import com.khrix.infrastructure.http.controllers.user.resources.UserResource
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserInputDto
import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.core.HttpResult
import com.khrix.infrastructure.security.UserClaims
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class UserRouting(
    private val updateSelfUserHandler: UpdateSelfUserHandler,
    private val getSelfUserHandler: GetSelfUserHandler
) : AppRouting {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            authenticate("auth-jwt") {
                get<UserResource> {
                    try {
                        val claims = UserClaims.getClaims(call)
                        call.respond(getSelfUserHandler.handler(claims))
                    } catch (exception: Exception) {
                        call.respond(
                            HttpResult(
                                null, HttpStatusCode.BadRequest, listOf(exception.message ?: "An error occurred")
                            )
                        )
                    }
                }
                put<UserResource.Update> {
                    try {
                        val body = call.receive<UserInputDto>()
                        val claims = UserClaims.getClaims(call)
                        call.respond(updateSelfUserHandler.handler(UpdateSelfUserHandlerBody(body, claims)))
                    } catch (exception: Exception) {
                        call.respond(
                            HttpResult(
                                null, HttpStatusCode.BadRequest, listOf(exception.message ?: "An error occurred")
                            )
                        )
                    }
                }
            }
        }
    }
}
