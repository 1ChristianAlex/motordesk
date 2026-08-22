package com.khrix

import com.khrix.adapter.app.configureHttp
import com.khrix.adapter.app.installInfrastructureDI
import com.khrix.adapter.redis.installRedisDI
import com.khrix.application.installApplicationDI
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.rootModule() {
    installInfrastructureDI()
    installApplicationDI()
    installRedisDI(dependencies, monitor)
    configureHttp()
}
