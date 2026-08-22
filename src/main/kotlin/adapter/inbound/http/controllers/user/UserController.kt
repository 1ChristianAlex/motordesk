package com.khrix.adapter.inbound.http.controllers.user

import com.khrix.adapter.inbound.http.controllers.core.AppController
import com.khrix.adapter.inbound.http.controllers.core.getBody
import com.khrix.adapter.inbound.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.adapter.inbound.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.adapter.inbound.http.controllers.user.handlers.UpdateSelfUserHandlerBody
import com.khrix.adapter.inbound.http.controllers.user.resources.UserResource
import com.khrix.adapter.inbound.http.controllers.user.resources.dto.UserInputDto
import com.khrix.adapter.security.UserClaims
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.routing.Routing
import io.ktor.server.routing.openapi.describe
import kotlinx.serialization.ExperimentalSerializationApi

class UserController(
    private val updateSelfUserHandler: UpdateSelfUserHandler,
    private val getSelfUserHandler: GetSelfUserHandler,
) : AppController() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            client {
                get<UserResource> {
                    val claims = UserClaims.getClaims(call)
                    call.send(getSelfUserHandler.handler(claims))
                }.describe(getSelfUserHandler::description)
                put<UserResource.Update> {
                    val body = getBody<UserInputDto>()
                    val claims = UserClaims.getClaims(call)
                    call.send(updateSelfUserHandler.handler(UpdateSelfUserHandlerBody(body, claims)))
                }.describe(updateSelfUserHandler::description)
            }
        }
    }
}
