package com.khrix.application

import com.khrix.application.company.usecase.CreateNewCompanyUseCaseImpl
import com.khrix.application.company.usecase.SearchCompanyByCnpjUseCaseImpl
import com.khrix.application.login.usecase.LoginUserUseCaseImpl
import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.application.register.usecase.VerifyIsEmailAvailableUseCaseImpl
import com.khrix.application.user.usecase.GetUserUseCaseImpl
import com.khrix.application.user.usecase.UpdateUserUseCaseImpl
import com.khrix.application.vehicles.usecase.CreateNewVehicleUseCaseImpl
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.domain.vehicle.model.CreateNewVehicleUseCase
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
    }
}