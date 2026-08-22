package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.port.usecase.DeleteServiceOrderCommand
import com.khrix.testutils.sampleServiceOrder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteServiceOrderUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val impl = DeleteServiceOrderUseCaseImpl(serviceOrderRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute throws when service order not found`() {
        runTest {
            coEvery { serviceOrderRepository.read(1) } returns null
            val res = impl.execute(DeleteServiceOrderCommand(complaint = "reason", serviceOrderId = 1))
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute deletes when found`() {
        runTest {
            val so = sampleServiceOrder()
            coEvery { serviceOrderRepository.read(so.id) } returns so
            coEvery { serviceOrderRepository.delete(so.id) } returns Unit

            impl.execute(DeleteServiceOrderCommand(complaint = "reason", serviceOrderId = so.id)).getOrThrow()
            coVerify { serviceOrderRepository.delete(so.id) }
        }
    }
}
