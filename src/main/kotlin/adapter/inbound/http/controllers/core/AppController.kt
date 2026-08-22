package com.khrix.adapter.inbound.http.controllers.core

import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.RoutingCall

abstract class AppController {
    abstract fun map(routing: Routing)

    suspend inline fun <reified Output> RoutingCall.send(httpResult: HttpResult<Output>) {
        response.status(httpResult.status)
        respond(httpResult)
    }

    fun Route.manager(build: Route.() -> Unit) {
        this.authenticate(AuthNames.AUTH_JWT_MANAGER, build = build)
    }

    fun Route.engineer(build: Route.() -> Unit) {
        this.authenticate(AuthNames.AUTH_JWT_ENGINEER, build = build)
    }

    fun Route.client(build: Route.() -> Unit) {
        this.authenticate(AuthNames.AUTHENTICATE, build = build)
    }
}
