package com.khrix.infrastructure.security

import com.auth0.jwt.algorithms.Algorithm
import com.khrix.infrastructure.app.loadProperties
import java.util.*

class JwtConfig {
    private val properties: Properties by lazy {
        loadProperties()
    }

    val issuer = properties.getProperty("jwt.issuer")

    val audience = properties.getProperty("jwt.audience")

    val realm = properties.getProperty("jwt.realm")

    private val secret = properties.getProperty("jwt.secret")

    val algorithm = Algorithm.HMAC256(secret)
}