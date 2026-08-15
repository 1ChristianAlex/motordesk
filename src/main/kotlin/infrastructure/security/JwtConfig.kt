package com.khrix.infrastructure.security

import com.auth0.jwt.algorithms.Algorithm
import com.khrix.infrastructure.app.loadProperties

class JwtConfig {
    private val properties by lazy {
        loadProperties()
    }

    val issuer = properties.jwtIssuer

    val audience = properties.jwtAudience

    val realm = properties.jwtRealm

    private val secret = properties.jwtSecret

    val algorithm = Algorithm.HMAC256(secret)
}
