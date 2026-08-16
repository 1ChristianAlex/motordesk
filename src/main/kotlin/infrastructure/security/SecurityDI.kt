package com.khrix.infrastructure.security

import com.khrix.domain.user.security.SecurityHasher
import com.khrix.domain.user.security.TokenService
import io.ktor.server.plugins.di.DependencyRegistry

fun securityDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<TokenService>(JwtTokenServiceImpl::class)
        provide<SecurityHasher>(SecurityHasherArgonImpl::class)
    }
}
