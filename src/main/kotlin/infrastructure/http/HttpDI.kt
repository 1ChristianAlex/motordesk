package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.controllers.login.LoginController
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandlerImpl
import com.khrix.infrastructure.http.controllers.register.RegisterController
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandlerImpl
import com.khrix.infrastructure.http.controllers.user.UserController
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandlerImpl
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandlerImpl
import com.khrix.infrastructure.http.controllers.vehicles.VehiclesController
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandlerImpl
import com.khrix.infrastructure.http.core.AppController
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateNewUserHandler>(CreateNewUserHandlerImpl::class)
        provide<LoginHandler>(LoginHandlerImpl::class)
        provide<GetSelfUserHandler>(GetSelfUserHandlerImpl::class)
        provide<UpdateSelfUserHandler>(UpdateSelfUserHandlerImpl::class)
        provide<CreateNewVehicleHandler>(CreateNewVehicleHandlerImpl::class)
        provide<List<AppController>> {
            listOf<AppController>(
                RegisterController(resolve()),
                LoginController(resolve()),
                UserController(resolve(), resolve()),
                VehiclesController(resolve())
            )
        }
    }
}