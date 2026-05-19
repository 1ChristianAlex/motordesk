package com.khrix.infrastructure.app

import com.khrix.infrastructure.exposed.appDatabase
import com.khrix.infrastructure.http.httpDI
import com.khrix.infrastructure.security.securityDI
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.appInfrastructure() {
    securityDI(dependencies)
    httpDI(dependencies)
    appDatabase(dependencies)
}