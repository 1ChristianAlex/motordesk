package com.khrix.infrastructure.http.core

import io.ktor.server.routing.*

interface AppRouting {
    fun map(
        routing: Routing
    )
}

