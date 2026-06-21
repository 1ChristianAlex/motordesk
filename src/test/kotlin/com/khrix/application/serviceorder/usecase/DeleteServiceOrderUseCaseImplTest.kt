package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith
import testutils.sampleServiceOrder

class DeleteServiceOrderUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val impl = DeleteServiceOrderUseCaseImpl(serviceOrderRepository)

    @Test
    fun `internalExecute throws when service order not found`() {
        runBlocking {
            coEvery { serviceOrderRepository.read(1) } returns null
            val res = impl.execute(com.khrix.domain.serviceorder.usecase.DeleteServiceOrderCommand(complaint = "reason", serviceOrderId = 1))
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute deletes when found`() {
        runBlocking {
            val so = sampleServiceOrder()
            coEvery { serviceOrderRepository.read(so.id) } returns so
            coEvery { serviceOrderRepository.delete(so.id) } returns Unit

            impl.execute(com.khrix.domain.serviceorder.usecase.DeleteServiceOrderCommand(complaint = "reason", serviceOrderId = so.id)).getOrThrow()
            coVerify { serviceOrderRepository.delete(so.id) }
        }
    }
}


