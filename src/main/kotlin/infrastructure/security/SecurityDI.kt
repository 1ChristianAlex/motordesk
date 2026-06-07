package com.khrix.infrastructure.security


import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.user.security.TokenService
import io.ktor.server.plugins.di.*

fun securityDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(JwtConfig::class)
        provide<TokenService>(JwtTokenServiceImpl::class)
        provide<PasswordHasher>(PasswordHasherArgonImpl::class)
    }
}