package com.khrix.application.user.usecase

import com.khrix.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import testutils.sampleUser

class GetUserUseCaseImplTest {
    private val userRepository = mockk<UserRepository>()
    private val impl = GetUserUseCaseImpl(userRepository)

    @Test
    fun `internalExecute returns user when found`() {
        runBlocking {
            val user = sampleUser()
            coEvery { userRepository.read(user.id) } returns user

            val res = impl.execute(user.id)
            assertEquals(user, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runBlocking {
            coEvery { userRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<com.khrix.domain.user.usecase.UserNotFoundException> { res.getOrThrow() }
        }
    }
}


