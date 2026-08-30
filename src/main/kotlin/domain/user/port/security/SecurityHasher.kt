package com.khrix.domain.user.port.security

interface SecurityHasher {
    fun hash(password: String): String

    fun verify(
        password: String,
        hash: String,
    ): Boolean

    fun isHashedPassword(password: String): Boolean
}
