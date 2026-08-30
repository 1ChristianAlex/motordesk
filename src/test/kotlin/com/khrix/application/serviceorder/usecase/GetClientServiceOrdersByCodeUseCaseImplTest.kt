package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeCommand
import com.khrix.domain.serviceorder.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.testutils.sampleServiceOrder
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetClientServiceOrdersByCodeUseCaseImplTest {
    private val getByCode = mockk<GetServiceOrdersByCodeUseCase>()
    private val useCase = GetClientServiceOrdersByCodeUseCaseImpl(getByCode)

    @Test
    fun `returns an order owned by the requesting client`() =
        runTest {
            val order = sampleServiceOrder()
            coEvery { getByCode.execute("#code") } returns Result.success(order)
            val command = GetClientServiceOrdersByCodeCommand("#code", order.client.id)
            assertEquals(order, useCase.execute(command).getOrThrow())
        }

    @Test
    fun `rejects an order owned by another client`() =
        runTest {
            val order = sampleServiceOrder()
            coEvery { getByCode.execute("#code") } returns Result.success(order)
            val command = GetClientServiceOrdersByCodeCommand("#code", order.client.id + 1)
            assertFailsWith<UnsupportedOperationException> { useCase.execute(command).getOrThrow() }
        }
}
