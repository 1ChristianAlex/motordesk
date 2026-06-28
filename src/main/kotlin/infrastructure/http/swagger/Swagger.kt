package com.khrix.infrastructure.http.swagger

import io.ktor.http.*
import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.*

fun Application.applySwagger() {

    routing {
        val info = OpenApiInfo("Motor Desk API", "1.0")
        val source = OpenApiDocSource.Routing(ContentType.Application.Json) {
            routingRoot.descendants()
        }
        swaggerUI("/swaggerUI") {
            this.info = info
            this.source = source
        }
    }
}