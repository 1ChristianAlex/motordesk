package com.khrix.infrastructure.http.controllers.user

import com.khrix.infrastructure.http.controllers.core.AuthNames
import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandlerBody
import com.khrix.infrastructure.http.controllers.user.resources.UserResource
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserInputDto
import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.security.UserClaims
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class UserController(
    private val updateSelfUserHandler: UpdateSelfUserHandler,
    private val getSelfUserHandler: GetSelfUserHandler
) : AppController() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            client {
                get<UserResource> {
                    val claims = UserClaims.getClaims(call)
                    call.send(getSelfUserHandler.handler(claims))
                }
                put<UserResource.Update> {
                    val body = getBody<UserInputDto>()
                    val claims = UserClaims.getClaims(call)
                    call.send(updateSelfUserHandler.handler(UpdateSelfUserHandlerBody(body, claims)))
                }
            }
        }
    }
}
