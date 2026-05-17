package com.khrix

import com.khrix.infrastructure.application.applicationDI
import com.khrix.infrastructure.application.configureHttp
import io.ktor.server.application.*

fun Application.rootModule() {
    applicationDI()
    configureHttp()
}
