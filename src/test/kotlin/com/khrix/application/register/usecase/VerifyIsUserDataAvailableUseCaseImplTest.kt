package com.khrix.application.register.usecase

import com.khrix.application.user.usecase.VerifyIsUserDataAvailableUseCaseImpl
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.valueobject.user.Email
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerifyIsUserDataAvailableUseCaseImplTest {
    private val userRepository = mockk<UserRepository>()
    private val impl = VerifyIsUserDataAvailableUseCaseImpl(userRepository)

    @Test
    fun `useCaseDescription returns expected string`() = runBlocking {
        assert(impl.useCaseDescription().contains("Check if email is available"))
    }

    @Test
    fun `returns true when email not found`() = runBlocking {
        val email = Email("new.user@example.com")
        coEvery { userRepository.getByEmail(email) } returns null

        val result = impl.execute(email)
        assertTrue(result.getOrThrow())
    }

    @Test
    fun `returns false when email found`() = runBlocking {
        val email = Email("existing@example.com")
        coEvery { userRepository.getByEmail(email) } returns com.khrix.domain.user.model.User(
            id = 1,
            addressId = 1,
            companyId = null,
            firstName = com.khrix.domain.valueobject.user.Name("Christian"),
            lastName = com.khrix.domain.valueobject.user.Name("Testmaster"),
            email = email,
            password = com.khrix.domain.valueobject.user.Password("Passw0rd!", true),
            phone = com.khrix.domain.valueobject.user.Phone("+1234567890"),
            cpf = com.khrix.domain.valueobject.user.CPF("11144477735"),
            isActive = true,
            role = com.khrix.domain.user.model.Role.CLIENT,
            createdAt = kotlinx.datetime.LocalDateTime(2020,1,1,0,0),
            updatedAt = kotlinx.datetime.LocalDateTime(2020,1,1,0,0)
        )

        val result = impl.execute(email)
        assertFalse(result.getOrThrow())
    }
}

