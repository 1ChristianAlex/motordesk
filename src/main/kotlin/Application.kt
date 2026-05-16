package com.khrix

import io.ktor.server.application.Application

fun Application.rootModule() {
    configurePostgres()
    configureExposed()
    configureDependencyInjection()
    configureRequestValidation()
    configureHttp()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
