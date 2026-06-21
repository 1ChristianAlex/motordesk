package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleServiceOrder
import kotlin.test.Test
import kotlin.test.assertEquals

class GetServiceOrdersByClientIdUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val impl = GetServiceOrdersByClientIdUseCaseImpl(serviceOrderRepository)

    @Test
    fun `internalExecute returns list of service orders`() = runBlocking {
        val so = sampleServiceOrder()
        coEvery { serviceOrderRepository.getByClientId(so.client.id) } returns listOf(so)

        val res = impl.execute(so.client.id)
        assertEquals(listOf(so), res.getOrThrow())
    }
}

