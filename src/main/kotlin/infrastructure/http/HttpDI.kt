package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.controllers.register.RegisterRouting
import com.khrix.infrastructure.http.controllers.register.handlers.CreateNewUserHandler
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.core.HTTPHandler
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(RegisterRouting::class)
        provide<List<AppRouting>> {
            listOf<AppRouting>(resolve<RegisterRouting>())
        }
        provide<HTTPHandler<ClientRegisterDto, UserOutputDto>> {
            CreateNewUserHandler(resolve())
        }
    }
}