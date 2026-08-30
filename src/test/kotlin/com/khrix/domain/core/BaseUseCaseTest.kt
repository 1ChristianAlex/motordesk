package com.khrix.domain.core

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BaseUseCaseImplTest {
    @Test
    fun `execute wraps success and failure in Result`() =
        runTest {
            val success = StubUseCase { it * 2 }.execute(3)
            val failure = StubUseCase { throw IllegalStateException("failed") }.execute(3)

            assertEquals(6, success.getOrThrow())
            assertTrue(failure.isFailure)
            assertFailsWith<IllegalStateException> { failure.getOrThrow() }
        }

    private class StubUseCase(
        private val block: (Int) -> Int,
    ) : BaseUseCaseImpl<Int, Int>() {
        override suspend fun internalExecute(command: Int) = block(command)

        override suspend fun useCaseDescription() = "stub"
    }
}
