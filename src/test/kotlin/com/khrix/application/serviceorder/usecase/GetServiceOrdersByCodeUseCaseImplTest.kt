package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.testutils.sampleServiceOrder
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetServiceOrdersByCodeUseCaseImplTest {
    private val repository = mockk<ServiceOrderRepository>()
    private val useCase = GetServiceOrdersByCodeUseCaseImpl(repository)

    @Test
    fun `returns service order found by code`() =
        runTest {
            val order = sampleServiceOrder()
            coEvery { repository.getByCode("#code") } returns order
            assertEquals(order, useCase.execute("#code").getOrThrow())
        }

    @Test
    fun `fails when code does not exist`() =
        runTest {
            coEvery { repository.getByCode("#missing") } returns null
            assertFailsWith<NoSuchElementException> { useCase.execute("#missing").getOrThrow() }
        }
}
