package com.khrix.application

import com.khrix.application.register.usecase.CreateNewUserUseCaseImpl
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.applicationDI() {
    dependencies {
        provide<CreateNewUserUseCase> {
            CreateNewUserUseCaseImpl(
                userRepository = resolve(),
                passwordHasher = resolve(),
                addressRepository = resolve()
            )
        }
    }
}