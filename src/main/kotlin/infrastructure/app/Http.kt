package com.khrix.infrastructure.app

import com.khrix.infrastructure.http.httpApplication
import io.ktor.server.application.*

fun Application.configureHttp() {
    httpApplication()
}


