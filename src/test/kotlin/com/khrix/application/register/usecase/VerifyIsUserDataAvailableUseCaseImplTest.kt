package com.khrix.application.register.usecase

import com.khrix.application.user.usecase.VerifyIsUserDataAvailableUseCaseImpl
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCaseCommand
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VerifyIsUserDataAvailableUseCaseImplTest {
    private val userRepository = mockk<UserRepository>()
    private val impl = VerifyIsUserDataAvailableUseCaseImpl(userRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `returns true when email not found`() = runTest {
        val email = Email("new.user@example.com")
        val cpf = CPF("114.154.800-36")
        coEvery { userRepository.getByEmail(email) } returns null
        coEvery {
            userRepository.getByCpf(
                cpf
            )
        } returns null

        val result = impl.execute(
            VerifyIsUserDataAvailableUseCaseCommand(
                email,
                cpf
            )
        )
        assertNull(result.exceptionOrNull())
    }

    @Test
    fun `returns false when email found`() = runTest {
        val email = Email("existing@example.com")
        coEvery { userRepository.getByEmail(email) } returns com.khrix.domain.user.model.User(
            id = 1,
            addressId = 1,
            companyId = null,
            firstName = com.khrix.domain.valueobject.user.Name("Christian"),
            lastName = com.khrix.domain.valueobject.user.Name("Testmaster"),
            email = email,
            password = com.khrix.domain.valueobject.user.Password.Raw("Passw0rd!"),
            phone = com.khrix.domain.valueobject.user.Phone("+1234567890"),
            cpf = CPF("11144477735"),
            isActive = true,
            role = com.khrix.domain.user.model.Role.CLIENT,
            createdAt = kotlinx.datetime.LocalDateTime(2020, 1, 1, 0, 0),
            updatedAt = kotlinx.datetime.LocalDateTime(2020, 1, 1, 0, 0)
        )

        val result = impl.execute(VerifyIsUserDataAvailableUseCaseCommand(email, CPF("114.154.800-36")))
        assertTrue(result.exceptionOrNull() is Throwable)
    }
}

