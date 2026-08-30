package com.khrix.application.vehicles.di

import com.khrix.application.vehicles.usecase.CreateNewVehicleUseCaseImpl
import com.khrix.application.vehicles.usecase.DeleteVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByIdUseCaseImpl
import com.khrix.application.vehicles.usecase.GetVehicleByOwnerIdUseCaseImpl
import com.khrix.application.vehicles.usecase.UpdateVehicleUseCaseImpl
import com.khrix.domain.vehicle.port.usecase.CreateNewVehicleUseCase
import com.khrix.domain.vehicle.port.usecase.DeleteVehicleByIdUseCase
import com.khrix.domain.vehicle.port.usecase.GetVehicleByIdUseCase
import com.khrix.domain.vehicle.port.usecase.GetVehicleByOwnerIdUseCase
import com.khrix.domain.vehicle.port.usecase.UpdateVehicleUseCase
import io.ktor.server.plugins.di.DependencyRegistry

fun installVehicleDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateNewVehicleUseCase>(CreateNewVehicleUseCaseImpl::class)
        provide<DeleteVehicleByIdUseCase>(DeleteVehicleByIdUseCaseImpl::class)
        provide<GetVehicleByIdUseCase>(GetVehicleByIdUseCaseImpl::class)
        provide<GetVehicleByOwnerIdUseCase>(GetVehicleByOwnerIdUseCaseImpl::class)
        provide<UpdateVehicleUseCase>(UpdateVehicleUseCaseImpl::class)
    }
}
