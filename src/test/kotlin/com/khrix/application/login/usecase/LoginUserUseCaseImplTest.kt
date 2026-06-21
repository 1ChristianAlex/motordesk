package com.khrix.application.login.usecase

import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleUser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserUseCaseImplGeneratedTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val impl = LoginUserUseCaseImpl(userRepository, passwordHasher)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assertEquals("Hash password and create new user", impl.useCaseDescription())
        }
    }

    @Test
    fun `internalExecute throws when user not found`() {
        runBlocking {
            val cmd = LoginTypes.EmailCredentials(email = com.khrix.domain.valueobject.user.Email("x@x.com"), password = com.khrix.domain.valueobject.user.Password("Passw0rd!"))
            coEvery { userRepository.getByEmail(any()) } returns null

            assertFailsWith<com.khrix.domain.user.usecase.InvalidCredentialsException> {
                impl.execute(cmd).getOrThrow()
            }
        }
    }

    @Test
    fun `internalExecute returns user when credentials valid`() {
        runBlocking {
            val user = sampleUser()
            val cmd = LoginTypes.EmailCredentials(email = user.email, password = com.khrix.domain.valueobject.user.Password("rawPass!1234"))

            coEvery { userRepository.getByEmail(user.email) } returns user
            coEvery { passwordHasher.verify(any(), any()) } returns true

            val result = impl.execute(cmd)
            val returned = result.getOrThrow()
            assertEquals(user, returned)
            coVerify { passwordHasher.verify(any(), any()) }
        }
    }
}

