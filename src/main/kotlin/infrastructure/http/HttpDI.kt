package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.http.controllers.login.LoginController
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandler
import com.khrix.infrastructure.http.controllers.login.handlers.LoginHandlerImpl
import com.khrix.infrastructure.http.controllers.register.RegisterController
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandlerImpl
import com.khrix.infrastructure.http.controllers.serviceorder.ServiceOrderController
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.CreateNewServiceOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.CreateNewServiceOrderHandlerImpl
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetClientServiceOrderItemHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetClientServiceOrderItemHandlerImpl
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetClientServicesOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetClientServicesOrderHandlerImpl
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetServiceOrderItemHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetServiceOrderItemHandlerImpl
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.UpdateServiceOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.UpdateServiceOrderHandlerImpl
import com.khrix.infrastructure.http.controllers.user.UserController
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.GetSelfUserHandlerImpl
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandler
import com.khrix.infrastructure.http.controllers.user.handlers.UpdateSelfUserHandlerImpl
import com.khrix.infrastructure.http.controllers.vehicles.VehiclesController
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandlerImpl
import com.khrix.infrastructure.http.controllers.vehicles.handlers.DeleteVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.DeleteVehicleHandlerImpl
import com.khrix.infrastructure.http.controllers.vehicles.handlers.GetVehicleByOwnerHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.GetVehicleByOwnerHandlerImpl
import com.khrix.infrastructure.http.controllers.vehicles.handlers.UpdateVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.UpdateVehicleHandlerImpl
import io.ktor.events.*
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry, monitor: Events) {
    with(dependencies) {
        provide<CreateNewUserHandler>(CreateNewUserHandlerImpl::class)
        provide<LoginHandler>(LoginHandlerImpl::class)
        provide<GetSelfUserHandler>(GetSelfUserHandlerImpl::class)
        provide<UpdateSelfUserHandler>(UpdateSelfUserHandlerImpl::class)
        provide<CreateNewVehicleHandler>(CreateNewVehicleHandlerImpl::class)
        provide<DeleteVehicleHandler>(DeleteVehicleHandlerImpl::class)
        provide<GetVehicleByOwnerHandler>(GetVehicleByOwnerHandlerImpl::class)
        provide<UpdateVehicleHandler>(UpdateVehicleHandlerImpl::class)
        provide<CreateNewServiceOrderHandler>(CreateNewServiceOrderHandlerImpl::class)
        provide<UpdateServiceOrderHandler>(UpdateServiceOrderHandlerImpl::class)
        provide<GetClientServicesOrderHandler>(GetClientServicesOrderHandlerImpl::class)
        provide<GetClientServiceOrderItemHandler>(GetClientServiceOrderItemHandlerImpl::class)
        provide<GetServiceOrderItemHandler>(GetServiceOrderItemHandlerImpl::class)
        provide<List<AppController>> {
            listOf<AppController>(
                RegisterController(resolve()),
                LoginController(resolve()),
                UserController(resolve(), resolve()),
                VehiclesController(
                    createNewVehicleHandler = resolve(),
                    updateVehicleHandler = resolve(),
                    deleteVehicleHandler = resolve(),
                    getVehicleByOwnerHandler = resolve()
                ),
                ServiceOrderController(
                    createNewServiceOrderHandler = resolve(),
                    updateServiceOrderHandler = resolve(),
                    getClientServicesOrderHandler = resolve(),
                    getClientServiceOrderItemHandler = resolve(),
                    getServiceOrderItemHandler = resolve()
                )
            )
        }
    }
}