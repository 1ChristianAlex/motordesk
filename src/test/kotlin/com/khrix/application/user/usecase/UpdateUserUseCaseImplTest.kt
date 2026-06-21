package com.khrix.application.user.usecase

import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleUser
import kotlin.test.Test

class UpdateUserUseCaseImplTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val impl = UpdateUserUseCaseImpl(userRepository, passwordHasher)

    @Test
    fun `internalExecute hashes password when not already hashed`() = runBlocking {
        val user = sampleUser()
        coEvery { passwordHasher.isHashedPassword(user.password.value) } returns false
        coEvery { passwordHasher.hash(user.password.value) } returns "hashed"
        coEvery { userRepository.update(user.id, any()) } returns Unit

        impl.execute(user).getOrThrow()
        coVerify { passwordHasher.hash(user.password.value) }
        coVerify { userRepository.update(user.id, any()) }
    }

    @Test
    fun `internalExecute does not rehash when already hashed`() = runBlocking {
        val user = sampleUser()
        coEvery { passwordHasher.isHashedPassword(user.password.value) } returns true
        coEvery { userRepository.update(user.id, any()) } returns Unit

        impl.execute(user).getOrThrow()
        coVerify(exactly = 0) { passwordHasher.hash(any()) }
        coVerify { userRepository.update(user.id, any()) }
    }
}

