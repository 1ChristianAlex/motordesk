package com.khrix.application.login.usecase

import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.security.PasswordHasher
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUserUseCaseImplTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()

    private val instance = LoginUserUseCaseImpl(userRepository = userRepository, passwordHasher = passwordHasher)

    private val plainPassword = "password@123"
    private val email = Email("john@example.com")
    private val password = Password(plainPassword)
    private val cpf = CPF("114.154.800-36")
    private val user = User(
        id = 0,
        firstName = Name("John Doe"),
        lastName = Name("Smith"),
        email = email,
        phone = Phone("1234567890"),
        password = password,
        cpf = cpf,
        createdAt = getCurrentUtcDateTime(),
        updatedAt = getCurrentUtcDateTime(),
        addressId = 0,
        isActive = true,
        companyId = null
    )


    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        coEvery { userRepository.getByEmail(email) } returns user
        coEvery { userRepository.getByCpf(cpf) } returns user
        coEvery { passwordHasher.verify(any(), any()) } returns true
    }


    @Test
    fun `should login user - email`() = runTest {
        val logInInfo = instance.execute(
            LoginTypes.EmailCredentials(
                email = email,
                password = password
            )
        ).getOrNull()

        assertEquals(user, logInInfo)
    }

    @Test
    fun `should login user - password`() = runTest {
        val logInInfo = instance.execute(
            LoginTypes.CpfCredentials(
                cpf = cpf,
                password = password
            )
        ).getOrNull()

        assertEquals(user, logInInfo)
    }
}