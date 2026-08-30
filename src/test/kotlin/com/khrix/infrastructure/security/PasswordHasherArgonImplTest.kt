package com.khrix.infrastructure.security

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PasswordHasherArgonImplTest {
    private val hasher = PasswordHasherArgonImpl()

    @Test
    fun `should hash a password and return non-empty string`() {
        val password = "securePassword123"
        val hash = hasher.hash(password)

        assertTrue(hash.isNotEmpty(), "Hash should not be empty")
        assertTrue(hash.isNotBlank(), "Hash should not be blank")
    }

    @Test
    fun `should generate different hashes for the same password (due to salt)`() {
        val password = "myPassword123"
        val hash1 = hasher.hash(password)
        val hash2 = hasher.hash(password)

        assertNotEquals(hash1, hash2, "Two hashes of the same password should be different due to salt")
    }

    @Test
    fun `should verify correct password against its hash`() {
        val password = "correctPassword456"
        val hash = hasher.hash(password)

        val isValid = hasher.verify(password, hash)
        assertTrue(isValid, "Correct password should verify against its hash")
    }

    @Test
    fun `should reject incorrect password against hash`() {
        val password = "correctPassword456"
        val wrongPassword = "wrongPassword789"
        val hash = hasher.hash(password)

        val isValid = hasher.verify(wrongPassword, hash)
        assertFalse(isValid, "Incorrect password should not verify against hash")
    }

    @Test
    fun `should verify password with special characters`() {
        val password = "P@ssw0rd!#\$%^&*()"
        val hash = hasher.hash(password)

        val isValid = hasher.verify(password, hash)
        assertTrue(isValid, "Password with special characters should verify correctly")
    }

    @Test
    fun `should verify empty password`() {
        val password = ""
        val hash = hasher.hash(password)

        val isValid = hasher.verify(password, hash)
        assertTrue(isValid, "Empty password should verify against its hash")
    }

    @Test
    fun `should reject empty password against non-empty password hash`() {
        val password = "nonEmptyPassword"
        val emptyPassword = ""
        val hash = hasher.hash(password)

        val isValid = hasher.verify(emptyPassword, hash)
        assertFalse(isValid, "Empty password should not verify against non-empty password hash")
    }

    @Test
    fun `should handle long passwords`() {
        val password = "a".repeat(1000)
        val hash = hasher.hash(password)

        val isValid = hasher.verify(password, hash)
        assertTrue(isValid, "Long password should verify correctly")
    }

    @Test
    fun `should verify multiple different passwords`() {
        val passwords = listOf("password1", "password2", "password3")
        val hashes = passwords.map { hasher.hash(it) }

        passwords.zip(hashes).forEach { (password, hash) ->
            assertTrue(hasher.verify(password, hash), "Each password should verify against its own hash")
        }
    }

    @Test
    fun `should not verify password against different password's hash`() {
        val password1 = "password1"
        val password2 = "password2"
        val hash1 = hasher.hash(password1)

        val isValid = hasher.verify(password2, hash1)
        assertFalse(isValid, "Password2 should not verify against password1's hash")
    }

    @Test
    fun `should handle unicode characters in password`() {
        val password = "пароль密码🔐"
        val hash = hasher.hash(password)

        val isValid = hasher.verify(password, hash)
        assertTrue(isValid, "Unicode password should verify correctly")
    }
}
