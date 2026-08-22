package com.khrix.adapter.app

import com.khrix.adapter.exposed.appDatabase
import com.khrix.adapter.inbound.http.httpDI
import com.khrix.adapter.mongodb.appMongoDb
import com.khrix.adapter.outbound.azure.azureDI
import com.khrix.adapter.security.securityDI
import com.khrix.adapter.sqids.sqIdsDI
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.dependencies

private fun getLazyInfraCredentials(isDevelopment: Boolean): InfraConfig {
    val result by lazy {
        if (isDevelopment) InfraConfigDevImpl() else InfraConfigEnvImpl()
    }

    return result
}

fun Application.installInfrastructureDI() {
    val isDevelopment = developmentMode

    dependencies {
        provide<Boolean>(name = "isDevelopment") { isDevelopment }
    }

    val scope = InfraCoroutineScope()

    dependencies {
        provide<Application>("ktorApplication") {
            this@installInfrastructureDI
        }
        provide("infraScope") {
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
