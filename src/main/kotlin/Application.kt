package com.khrix

import com.khrix.application.installApplicationDI
import com.khrix.infrastructure.app.installInfrastructureDI
import com.khrix.infrastructure.http.installHttpDI
import com.khrix.infrastructure.app.configureHttp
import com.khrix.infrastructure.redis.installRedisDI
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.rootModule() {
    installInfrastructureDI()
    installApplicationDI()
    installRedisDI(dependencies, monitor)
    configureHttp()
}
