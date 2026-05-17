package com.khrix.infrastructure.application

import com.khrix.infrastructure.exposed.appDatabase
import com.khrix.infrastructure.http.httpDI
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.applicationDI() {
    httpDI(dependencies)
    appDatabase(dependencies)
}