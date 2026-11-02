package com.khrix

import com.khrix.adapter.app.configureHttp
import com.khrix.adapter.app.installAdapterDI
import com.khrix.adapter.outbound.redis.installRedisDI
import com.khrix.adapter.outbound.telemetry.setupServerTelemetry
import com.khrix.application.installApplicationDI
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.rootModule() {
    installAdapterDI()
    installApplicationDI()
    setupServerTelemetry()
    installRedisDI(dependencies, monitor)
    configureHttp()
}
