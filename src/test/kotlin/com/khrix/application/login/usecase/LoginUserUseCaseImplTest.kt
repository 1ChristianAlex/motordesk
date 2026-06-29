package com.khrix.application.login.usecase

import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.user.usecase.InvalidCredentialsException
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Password
import com.khrix.testutils.sampleUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserUseCaseImplGeneratedTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val impl = LoginUserUseCaseImpl(userRepository, passwordHasher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute throws when user not found`() {
        runTest {
            val email = Email("testman@email.com")
            val cmd =
                LoginTypes.EmailCredentials(
                    email = email,
                    password = Password.Raw("Passw0rd!"),
                )
            coEvery { userRepository.getByEmail(email) } returns null

            assertFailsWith<InvalidCredentialsException> {
                impl.execute(cmd).getOrThrow()
            }
        }
    }

    @Test
    fun `internalExecute returns user when credentials valid`() {
        runTest {
            val user = sampleUser()
            val cmd = LoginTypes.EmailCredentials(email = user.email, password = Password.Raw("rawPass!1234"))

            coEvery { userRepository.getByEmail(user.email) } returns user
            coEvery { passwordHasher.verify(any(), any()) } returns true

            val result = impl.execute(cmd)
            val returned = result.getOrThrow()
            assertEquals(user, returned)
            coVerify { passwordHasher.verify(any(), any()) }
        }
    }
}
