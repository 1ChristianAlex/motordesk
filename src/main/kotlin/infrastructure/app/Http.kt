package com.khrix.infrastructure.app

import com.khrix.infrastructure.http.httpApplication
import com.khrix.infrastructure.http.swagger.applySwagger
import io.ktor.server.application.Application

fun Application.configureHttp() {
    httpApplication()
    applySwagger()
}
