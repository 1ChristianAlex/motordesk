package com.khrix.domain.user.security

interface PasswordHasher {
    fun hash(
        password: String
    ): String

    fun verify(
        password: String,
        hash: String
    ): Boolean

    fun isHashedPassword(password: String): Boolean
}