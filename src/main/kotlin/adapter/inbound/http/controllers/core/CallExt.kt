package com.khrix.adapter.inbound.http.controllers.core

import com.khrix.adapter.inbound.http.controllers.core.exceptions.HandlerException
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext

suspend inline fun <reified T : Any> RoutingContext.getBody(): T =
    try {
        call.receive<T>()
    } catch (_: Exception) {
        throw HandlerException.BadRequest("Invalid request body")
    }
