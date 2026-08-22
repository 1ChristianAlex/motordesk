package com.khrix.application

import com.khrix.application.core.coroutine.ApplicationScope
import com.khrix.application.email.di.installEmailDI
import com.khrix.application.inventory.di.installInventoryDI
import com.khrix.application.serviceorder.di.installServiceOrderDI
import com.khrix.application.user.di.installUserDI
import com.khrix.application.vehicles.di.installVehicleDI
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.dependencies

fun Application.installApplicationDI() {
    val scope = ApplicationScope()
    dependencies {
        provide("applicationScope") {
            scope
        }
    }

    installUserDI(dependencies)
    installVehicleDI(dependencies)
    installInventoryDI(dependencies)
    installEmailDI(dependencies)
    installServiceOrderDI(dependencies)

    monitor.subscribe(ApplicationStopping) {
        scope.shutdown()
    }
}
