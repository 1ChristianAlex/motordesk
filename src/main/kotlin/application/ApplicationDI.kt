package com.khrix.application

import com.khrix.application.company.usecase.CreateNewCompanyUseCaseImpl
import com.khrix.application.company.usecase.SearchCompanyByCnpjUseCaseImpl
import com.khrix.application.inventory.CreateInventoryUseCaseImpl
import com.khrix.application.inventory.DecrementItemInventoryUseCaseImpl
import com.khrix.application.inventory.DeleteInventoryUseCaseImpl
import com.khrix.application.inventory.GetInventoryByIdOrSkuUseCaseImpl
import com.khrix.application.inventory.GetInventoryByListIdOrSkuUseCaseImpl
import com.khrix.application.inventory.UpdateInventoryUseCaseImpl
import com.khrix.application.login.usecase.LoginUserUseCaseImpl
import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.application.register.usecase.VerifyIsEmailAvailableUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.CreateTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.DeleteTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByListIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.UpdateTaskUseCaseImpl
import com.khrix.application.user.usecase.GetUserUseCaseImpl
import com.khrix.application.user.usecase.UpdateUserUseCaseImpl
import com.khrix.application.vehicles.usecase.CreateNewVehicleUseCaseImpl
import com.khrix.application.vehicles.usecase.DeleteVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByOwnerIdUseCaseImpl
import com.khrix.application.vehicles.usecase.UpdateVehicleUseCaseImpl
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
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
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.domain.vehicle.usecase.CreateNewVehicleUseCase
import com.khrix.domain.vehicle.usecase.DeleteVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByOwnerIdUseCase
import com.khrix.domain.vehicle.usecase.UpdateVehicleUseCase
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.applicationDI() {
    dependencies {
        provide<CreateNewUserUseCase>(CreateNewUserUseCaseImpl::class)
        provide<VerifyIsEmailAvailableUseCase>(VerifyIsEmailAvailableUseCaseImpl::class)
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
    }
}