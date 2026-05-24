package com.khrix.domain.user.model


import com.khrix.domain.valueobject.CPF
import com.khrix.domain.valueobject.Email
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginTypesTest {

    @Test
    fun `should create EmailCredentials when email is valid`() {

        val result = LoginTypes.create(
            userName = "john@doe.com",
            password = "password@1236"
        )

        assertTrue(result is LoginTypes.EmailCredentials)

        val credentials = result as LoginTypes.EmailCredentials

        assertEquals(
            Email("john@doe.com"),
            credentials.email
        )

        assertEquals(
            "password@1236",
            credentials.password.value
        )
    }

    @Test
    fun `should create CpfCredentials when cpf is valid`() {

        val result = LoginTypes.create(
            userName = "114.154.800-36",
            password = "password@123"
        )

        assertTrue(result is LoginTypes.CpfCredentials)

        val credentials = result as LoginTypes.CpfCredentials

        assertEquals(
            CPF("114.154.800-36"),
            credentials.cpf
        )

        assertEquals(
            "password@123",
            credentials.password.value
        )
    }

    @Test
    fun `should throw exception when username is invalid`() {

        runCatching {
            LoginTypes.create(
                userName = "invalid-user",
                password = "password@1236"
            )
        }.onFailure { exception ->
            assertTrue(exception is IllegalArgumentException)
        }
    }
}