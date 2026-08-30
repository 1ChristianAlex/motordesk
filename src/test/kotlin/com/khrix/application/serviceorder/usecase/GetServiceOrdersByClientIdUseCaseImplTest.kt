package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.testutils.sampleServiceOrder
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

class GetServiceOrdersByClientIdUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val impl = GetServiceOrdersByClientIdUseCaseImpl(serviceOrderRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns list of service orders`() =
        runTest {
            val so = sampleServiceOrder()
            coEvery { serviceOrderRepository.getByClientId(so.client.id) } returns listOf(so)

            val res = impl.execute(so.client.id)
            assertEquals(listOf(so), res.getOrThrow())
        }
}
