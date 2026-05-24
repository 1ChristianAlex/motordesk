package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.controllers.login.LoginRouting
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandlerImpl
import com.khrix.infrastructure.http.controllers.register.RegisterRouting
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandlerImpl
import com.khrix.infrastructure.http.core.AppRouting
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateNewUserHandler>(CreateNewUserHandlerImpl::class)
        provide<LoginHandler>(LoginHandlerImpl::class)
        provide<List<AppRouting>> {
            listOf<AppRouting>(
                RegisterRouting(resolve()),
                LoginRouting(resolve()),
            )
        }
    }
}