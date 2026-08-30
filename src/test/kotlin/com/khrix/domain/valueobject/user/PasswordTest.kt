package com.khrix.domain.valueobject.user

import com.khrix.domain.valueobject.ValidationErrorResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PasswordTest {
    @Test
    fun `raw password requires length letter number and special character`() {
        assertEquals("Passw0rd!", Password.Raw("Passw0rd!").value)
        assertFailsWith<ValidationErrorResult> { Password.Raw("password") }
    }

    @Test
    fun `hashed password is accepted as opaque data`() = assertEquals("hash", Password.Hashed("hash").value)
}
