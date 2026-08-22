package com.khrix.adapter.app

import com.khrix.adapter.http.httpApplication
import com.khrix.adapter.http.swagger.applySwagger
import io.ktor.server.application.Application

fun Application.configureHttp() {
    httpApplication()
    applySwagger()
}
