package com.khrix.adapter.app

import com.khrix.adapter.inbound.http.httpDI
import com.khrix.adapter.outbound.azure.azureDI
import com.khrix.adapter.outbound.exposed.appDatabase
import com.khrix.adapter.outbound.mongodb.appMongoDb
import com.khrix.adapter.outbound.security.securityDI
import com.khrix.adapter.outbound.sqids.sqIdsDI
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.dependencies

private fun getLazyInfraCredentials(isDevelopment: Boolean): InfraConfig {
    val result by lazy {
        if (isDevelopment) InfraConfigDevImpl() else InfraConfigEnvImpl()
    }

    return result
}

fun Application.installAdapterDI() {
    val isDevelopment = developmentMode

    dependencies {
        provide<Boolean>(name = "isDevelopment") { isDevelopment }
    }

    val scope = InfraCoroutineScope()

    dependencies {
        provide<Application>("ktorApplication") {
            this@installAdapterDI
        }
        provide("adapterScope") {
            scope
        }
        provide<InfraConfig> {
            getLazyInfraCredentials(isDevelopment)
        }
    }

    monitor.subscribe(
        ApplicationStopping,
    ) {
        scope.shutdown()
    }

    azureDI(dependencies)
    sqIdsDI(dependencies)
    appDatabase(dependencies)
    appMongoDb(dependencies, monitor)
    securityDI(dependencies)
    httpDI(dependencies)
}
