package com.khrix

import com.khrix.application.applicationDI
import com.khrix.infrastructure.app.appInfrastructure
import com.khrix.infrastructure.app.configureHttp
import io.ktor.server.application.*

fun Application.rootModule() {
    appInfrastructure()
    applicationDI()
    configureHttp()
}
