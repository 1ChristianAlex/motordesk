package com.khrix.application.user.di

import com.khrix.application.login.usecase.LoginUserUseCaseImpl
import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.application.user.usecase.GetUserUseCaseImpl
import com.khrix.application.user.usecase.UpdateUserUseCaseImpl
import com.khrix.application.user.usecase.VerifyIsUserDataAvailableUseCaseImpl
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.port.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.user.port.usecase.CreateNewUserUseCase
import com.khrix.domain.user.port.usecase.GetUserUseCase
import com.khrix.domain.user.port.usecase.LoginUserUseCase
import com.khrix.domain.user.port.usecase.UpdateUserUseCase
import com.khrix.domain.user.port.usecase.VerifyIsUserDataAvailableUseCase
import io.ktor.server.plugins.di.DependencyRegistry

fun installUserDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateNewUserUseCase>(CreateNewUserUseCaseImpl::class)
        provide<VerifyIsUserDataAvailableUseCase>(VerifyIsUserDataAvailableUseCaseImpl::class)
        provide<SearchCompanyByCnpjUseCase>(com.khrix.application.company.usecase.SearchCompanyByCnpjUseCaseImpl::class)
        provide<CreateNewCompanyUseCase>(com.khrix.application.company.usecase.CreateNewCompanyUseCaseImpl::class)
        provide<LoginUserUseCase>(LoginUserUseCaseImpl::class)
        provide<GetUserUseCase>(GetUserUseCaseImpl::class)
        provide<UpdateUserUseCase>(UpdateUserUseCaseImpl::class)
    }
}
