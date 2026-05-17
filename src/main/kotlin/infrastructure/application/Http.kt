package com.khrix.infrastructure.application

import com.khrix.infrastructure.http.configureSecurityJWT
import com.khrix.infrastructure.http.httpApplication
import com.khrix.infrastructure.http.httpDI
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.configureHttp() {
    httpDI(dependencies)
    configureSecurityJWT()
    httpApplication()
}
