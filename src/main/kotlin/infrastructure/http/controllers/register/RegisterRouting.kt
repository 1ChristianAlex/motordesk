package com.khrix.infrastructure.http.controllers.register

import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.controllers.register.resources.RegisterResource
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class RegisterRouting : AppRouting {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            post<RegisterResource> {
            }
        }
    }
}
