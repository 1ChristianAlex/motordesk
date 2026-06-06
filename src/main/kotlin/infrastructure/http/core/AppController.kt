package com.khrix.infrastructure.http.core

import io.ktor.server.response.*
import io.ktor.server.routing.*

abstract class AppController {
    abstract fun map(
        routing: Routing
    )

    suspend inline fun <reified Output> RoutingCall.send(httpResult: HttpResult<Output>) {
        response.status(httpResult.status)
        respond(httpResult)
    }
}

