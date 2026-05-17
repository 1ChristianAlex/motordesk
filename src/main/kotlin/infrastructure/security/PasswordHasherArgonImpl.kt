package com.khrix.infrastructure.security

import com.khrix.domain.security.PasswordHasher
import de.mkammerer.argon2.Argon2Factory

class PasswordHasherArgonImpl : PasswordHasher {
    private val argon2 = Argon2Factory.create()

    override fun hash(password: String): String {
        val iterations = 3
        val memory = 65536
        val parallelism = 1

        return argon2.hash(
            iterations,
            memory,
            parallelism,
            password.toCharArray()
        )
    }

    override fun verify(password: String, hash: String): Boolean {
        return try {
            argon2.verify(hash, password.toCharArray())
        } finally {
            argon2.wipeArray(password.toCharArray())
        }
    }
}