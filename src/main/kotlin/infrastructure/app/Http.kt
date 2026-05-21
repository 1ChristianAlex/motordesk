package com.khrix.infrastructure.app

import com.khrix.infrastructure.http.configureSecurityJWT
import com.khrix.infrastructure.http.httpApplication
import io.ktor.server.application.*

fun Application.configureHttp() {
    configureSecurityJWT()
    httpApplication()
}


