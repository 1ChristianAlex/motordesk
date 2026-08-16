package com.khrix.infrastructure.app

import com.khrix.infrastructure.azure.azureDI
import com.khrix.infrastructure.exposed.appDatabase
import com.khrix.infrastructure.http.httpDI
import com.khrix.infrastructure.mongodb.appMongoDb
import com.khrix.infrastructure.security.securityDI
import com.khrix.infrastructure.sqids.sqIdsDI
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.dependencies

private fun getLazyInfraCredentials(isDevelopment: Boolean): InfraConfig {
    val result by lazy {
        if (isDevelopment) InfraConfigDevImpl() else InfraConfigEnvImpl()
    }

    return result
}

fun Application.appInfrastructure() {
    val isDevelopment = developmentMode

    dependencies {
        provide<Boolean>(name = "isDevelopment") { isDevelopment }
    }

    val scope = InfraCoroutineScope()

    dependencies {
        provide<Application>("ktorApplication") {
            this@appInfrastructure
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
