package com.khrix.infrastructure.http.controllers.core

import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import io.ktor.server.request.*
import io.ktor.server.routing.*

suspend inline fun <reified T : Any> RoutingContext.getBody(): T {
    return try {
        call.receive<T>()
    } catch (error: Exception) {
        throw HandlerException.BadRequest(error.cause ?: error)
    }
}