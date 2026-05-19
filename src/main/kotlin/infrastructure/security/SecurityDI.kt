package com.khrix.infrastructure.security


import com.khrix.domain.security.PasswordHasher
import io.ktor.server.plugins.di.*

fun securityDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<PasswordHasher> {
            PasswordHasherArgonImpl()
        }
    }
}