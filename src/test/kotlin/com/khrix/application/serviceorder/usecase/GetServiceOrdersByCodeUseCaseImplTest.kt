package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.usecase.GetServiceOrderHistoryUseCase
import com.khrix.testutils.sampleServiceOrder
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetServiceOrdersByCodeUseCaseImplTest {
    private val repository = mockk<ServiceOrderRepository>()
    private val getServiceOrderHistoryUseCase = mockk<GetServiceOrderHistoryUseCase>()
    private val useCase = GetServiceOrdersByCodeUseCaseImpl(repository, getServiceOrderHistoryUseCase)

    @Test
    fun `returns service order found by code`() =
        runTest {
            val order = sampleServiceOrder()
            coEvery { getServiceOrderHistoryUseCase.execute(order.id) } returns Result.success(emptyList())
            coEvery { repository.getByCode("#code") } returns order
            assertEquals(order, useCase.execute("#code").getOrThrow().serviceOrder)
        }

    @Test
    fun `fails when code does not exist`() =
        runTest {
            coEvery { repository.getByCode("#missing") } returns null
            assertFailsWith<NoSuchElementException> { useCase.execute("#missing").getOrThrow() }
        }
}
