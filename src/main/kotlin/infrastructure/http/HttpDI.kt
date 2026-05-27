package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.controllers.login.LoginRouting
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandlerImpl
import com.khrix.infrastructure.http.controllers.register.RegisterRouting
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandlerImpl
import com.khrix.infrastructure.http.controllers.user.UserRouting
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandlerImpl
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandlerImpl
import com.khrix.infrastructure.http.core.AppRouting
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateNewUserHandler>(CreateNewUserHandlerImpl::class)
        provide<LoginHandler>(LoginHandlerImpl::class)
        provide<GetSelfUserHandler>(GetSelfUserHandlerImpl::class)
        provide<UpdateSelfUserHandler>(UpdateSelfUserHandlerImpl::class)
        provide<List<AppRouting>> {
            listOf<AppRouting>(
                RegisterRouting(resolve()),
                LoginRouting(resolve()),
                UserRouting(resolve(), resolve())
            )
        }
    }
}