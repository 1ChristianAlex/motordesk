package com.khrix.adapter.http.swagger

import io.ktor.http.ContentType
import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.Application
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.openapi.OpenApiDocSource
import io.ktor.server.routing.routing
import io.ktor.server.routing.routingRoot

fun Application.applySwagger() {
    routing {
        val info = OpenApiInfo("Motor Desk API", "1.0")
        val source =
            OpenApiDocSource.Routing(ContentType.Application.Json) {
                routingRoot.descendants()
            }
        swaggerUI("/swaggerUI") {
            this.info = info
            this.source = source
        }

        openAPI(path = "openapi") {
            this.info = info
            this.source = source
        }
    }
}
