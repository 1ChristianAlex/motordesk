package com.khrix.infrastructure.http


import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.register.RegisterRouting
import io.ktor.server.plugins.di.*

fun httpDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(RegisterRouting::class)
        provide<List<AppRouting>> {
            listOf<AppRouting>(resolve<RegisterRouting>())
        }
    }
}