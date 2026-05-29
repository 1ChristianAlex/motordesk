package com.khrix.infrastructure.http.core

import io.ktor.server.routing.*

interface AppController {
    fun map(
        routing: Routing
    )
}

