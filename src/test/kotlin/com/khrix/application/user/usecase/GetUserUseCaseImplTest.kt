package com.khrix.application.user.usecase

import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.UserNotFoundException
import com.khrix.testutils.sampleUser
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserUseCaseImplTest {
    private val userRepository = mockk<UserRepository>()
    private val impl = GetUserUseCaseImpl(userRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns user when found`() {
        runTest {
            val user = sampleUser()
            coEvery { userRepository.read(user.id) } returns user

            val res = impl.execute(user.id)
            assertEquals(user, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runTest {
            coEvery { userRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<UserNotFoundException> { res.getOrThrow() }
        }
    }
}
