package com.khrix.adapter.outbound.security

import com.khrix.domain.user.port.security.SecurityHasher
import com.khrix.domain.user.port.security.TokenService
import io.ktor.server.plugins.di.DependencyRegistry

fun securityDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<TokenService>(JwtTokenServiceImpl::class)
        provide<SecurityHasher>(SecurityHasherArgonImpl::class)
    }
}
