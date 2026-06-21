package com.khrix.application.user.usecase

import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import testutils.sampleUser
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateUserUseCaseImplTest {
    private val verifyIsUserDataAvailableUseCase: VerifyIsUserDataAvailableUseCase = mockk()
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val impl = UpdateUserUseCaseImpl(userRepository, passwordHasher, verifyIsUserDataAvailableUseCase)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `internalExecute hashes password when not already hashed`() = runTest {
        val user = sampleUser()
        coEvery { passwordHasher.isHashedPassword(user.password.value) } returns false
        coEvery { passwordHasher.hash(user.password.value) } returns "hashed"
        coEvery { userRepository.update(user.id, any()) } returns Unit
        coJustRun {
            verifyIsUserDataAvailableUseCase.execute(any())
        }
        impl.execute(user).getOrThrow()
        coVerify { passwordHasher.hash(user.password.value) }
        coVerify { userRepository.update(user.id, any()) }
    }

    @Test
    fun `internalExecute does not rehash when already hashed`() = runTest {
        val user = sampleUser()
        coEvery { passwordHasher.isHashedPassword(user.password.value) } returns true
        coEvery { userRepository.update(user.id, any()) } returns Unit
        coJustRun {
            verifyIsUserDataAvailableUseCase.execute(any())
        }
        impl.execute(user).getOrThrow()
        coVerify(exactly = 0) { passwordHasher.hash(any()) }
        coVerify { userRepository.update(user.id, any()) }
    }
}

