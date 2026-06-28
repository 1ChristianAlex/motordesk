package com.khrix.application

import com.khrix.application.company.usecase.CreateNewCompanyUseCaseImpl
import com.khrix.application.company.usecase.SearchCompanyByCnpjUseCaseImpl
import com.khrix.application.core.coroutine.ApplicationScope
import com.khrix.application.email.usecase.CreateEmailQueueUseCaseImpl
import com.khrix.application.email.usecase.UpdateEmailQueueUseCaseImpl
import com.khrix.application.inventory.CreateInventoryUseCaseImpl
import com.khrix.application.inventory.DecrementItemInventoryUseCaseImpl
import com.khrix.application.inventory.DeleteInventoryUseCaseImpl
import com.khrix.application.inventory.GetInventoryByIdOrSkuUseCaseImpl
import com.khrix.application.inventory.GetInventoryByListIdOrSkuUseCaseImpl
import com.khrix.application.inventory.UpdateInventoryUseCaseImpl
import com.khrix.application.login.usecase.LoginUserUseCaseImpl
import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.CreateTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.DeleteTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByListIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.UpdateTaskUseCaseImpl
import com.khrix.application.serviceorder.usecase.CreateServiceOrderUseCaseImpl
import com.khrix.application.serviceorder.usecase.DeleteServiceOrderUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetClientServiceOrdersByCodeUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetServiceOrdersByClientIdUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetServiceOrdersByCodeUseCaseImpl
import com.khrix.application.serviceorder.usecase.UpdateServiceOrderTaskUseCaseImpl
import com.khrix.application.serviceorder.usecase.UpdateServiceOrderUseCaseImpl
import com.khrix.application.user.usecase.GetUserUseCaseImpl
import com.khrix.application.user.usecase.UpdateUserUseCaseImpl
import com.khrix.application.user.usecase.VerifyIsUserDataAvailableUseCaseImpl
import com.khrix.application.vehicles.usecase.CreateNewVehicleUseCaseImpl
import com.khrix.application.vehicles.usecase.DeleteVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByOwnerIdUseCaseImpl
import com.khrix.application.vehicles.usecase.UpdateVehicleUseCaseImpl
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.email.usecase.UpdateEmailQueueUseCase
import com.khrix.domain.inventory.usecase.CreateInventoryUseCase
import com.khrix.domain.inventory.usecase.DecrementItemInventoryUseCase
import com.khrix.domain.inventory.usecase.DeleteInventoryUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByIdOrSkuUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.inventory.usecase.UpdateInventoryUseCase
import com.khrix.domain.serviceorder.task.usecase.CreateTaskUseCase
import com.khrix.domain.serviceorder.task.usecase.DeleteTaskUseCase
import com.khrix.domain.serviceorder.task.usecase.GetTaskByIdUseCase
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.task.usecase.UpdateTaskUseCase
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderUseCase
import com.khrix.domain.serviceorder.usecase.DeleteServiceOrderUseCase
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByClientIdUseCase
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderTaskUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.vehicle.usecase.CreateNewVehicleUseCase
import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByOwnerIdUseCase
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.applicationDI() {
    val scope = ApplicationScope()
    dependencies {
        provide("applicationScope") {
            scope
        }
        provide<CreateNewUserUseCase>(CreateNewUserUseCaseImpl::class)
        provide<VerifyIsUserDataAvailableUseCase>(VerifyIsUserDataAvailableUseCaseImpl::class)
        provide<SearchCompanyByCnpjUseCase>(SearchCompanyByCnpjUseCaseImpl::class)
        provide<CreateNewCompanyUseCase>(CreateNewCompanyUseCaseImpl::class)
        provide<LoginUserUseCase>(LoginUserUseCaseImpl::class)
        provide<GetUserUseCase>(GetUserUseCaseImpl::class)
        provide<UpdateUserUseCase>(UpdateUserUseCaseImpl::class)
        provide<CreateNewVehicleUseCase>(CreateNewVehicleUseCaseImpl::class)
        provide<DeleteVehicleByIdUseCase>(DeleteVehicleByIdUseCaseImpl::class)
        provide<GetVehicleByIdUseCase>(GetVehicleByIdUseCaseImpl::class)
        provide<GetVehicleByOwnerIdUseCase>(GetVehicleByOwnerIdUseCaseImpl::class)
        provide<UpdateVehicleUseCase>(UpdateVehicleUseCaseImpl::class)
        provide<CreateTaskUseCase>(CreateTaskUseCaseImpl::class)
        provide<DeleteTaskUseCase>(DeleteTaskUseCaseImpl::class)
        provide<GetTaskByIdUseCase>(GetTaskByIdUseCaseImpl::class)
        provide<UpdateTaskUseCase>(UpdateTaskUseCaseImpl::class)
        provide<GetTaskByListIdUseCase>(GetTaskByListIdUseCaseImpl::class)
        provide<CreateInventoryUseCase>(CreateInventoryUseCaseImpl::class)
        provide<DecrementItemInventoryUseCase>(DecrementItemInventoryUseCaseImpl::class)
        provide<GetInventoryByListIdOrSkuUseCase>(GetInventoryByListIdOrSkuUseCaseImpl::class)
        provide<DeleteInventoryUseCase>(DeleteInventoryUseCaseImpl::class)
        provide<GetInventoryByIdOrSkuUseCase>(GetInventoryByIdOrSkuUseCaseImpl::class)
        provide<UpdateInventoryUseCase>(UpdateInventoryUseCaseImpl::class)
        provide<CreateEmailQueueUseCase>(CreateEmailQueueUseCaseImpl::class)
        provide<CreateServiceOrderUseCase>(CreateServiceOrderUseCaseImpl::class)
        provide<DeleteServiceOrderUseCase>(DeleteServiceOrderUseCaseImpl::class)
        provide<GetServiceOrdersByClientIdUseCase>(GetServiceOrdersByClientIdUseCaseImpl::class)
        provide<UpdateServiceOrderUseCase>(UpdateServiceOrderUseCaseImpl::class)
        provide<UpdateServiceOrderTaskUseCase>(UpdateServiceOrderTaskUseCaseImpl::class)
        provide<GetServiceOrdersByCodeUseCase>(GetServiceOrdersByCodeUseCaseImpl::class)
        provide<GetClientServiceOrdersByCodeUseCase>(GetClientServiceOrdersByCodeUseCaseImpl::class)
        provide<UpdateEmailQueueUseCase>(UpdateEmailQueueUseCaseImpl::class)
    }

    monitor.subscribe(
        ApplicationStopping
    ) {
        scope.shutdown()
    }
}