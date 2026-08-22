package com.khrix.application.user.di

import com.khrix.application.login.usecase.LoginUserUseCaseImpl
import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.application.user.usecase.GetUserUseCaseImpl
import com.khrix.application.user.usecase.UpdateUserUseCaseImpl
import com.khrix.application.user.usecase.VerifyIsUserDataAvailableUseCaseImpl
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.LoginUserUseCase
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
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
