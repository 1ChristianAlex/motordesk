package com.khrix.infrastructure.app

import com.khrix.infrastructure.exposed.appDatabase
import com.khrix.infrastructure.http.httpDI
import com.khrix.infrastructure.mongodb.appMongoDb
import com.khrix.infrastructure.security.securityDI
import com.khrix.infrastructure.sqids.sqIdsDI
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.appInfrastructure() {
    val isDevelopment = developmentMode

    dependencies {
        provide<Boolean>(name = "isDevelopment") { isDevelopment }
    }
    sqIdsDI(dependencies)
    appDatabase(dependencies)
    appMongoDb(dependencies)
    securityDI(dependencies)
    httpDI(dependencies)
}