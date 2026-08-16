package com.khrix

import com.khrix.application.applicationDI
import com.khrix.infrastructure.app.appInfrastructure
import com.khrix.infrastructure.app.configureHttp
import com.khrix.infrastructure.redis.redisLettuceDi
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.rootModule() {
    appInfrastructure()
    applicationDI()
    redisLettuceDi(dependencies, monitor)
    configureHttp()
}
