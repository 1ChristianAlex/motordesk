package com.khrix.application.serviceorder.usecase

import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import testutils.sampleServiceOrder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateServiceOrderUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val getInventoryByListIdOrSkuUseCase = mockk<GetInventoryByListIdOrSkuUseCase>()
    private val getTaskByListIdUseCase = mockk<GetTaskByListIdUseCase>()

    private val impl = UpdateServiceOrderUseCaseImpl(
        serviceOrderRepository,
        getInventoryByListIdOrSkuUseCase,
        getTaskByListIdUseCase
    )
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `internalExecute updates service order`() = runTest {
        val existing = sampleServiceOrder()
        val command = com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand(
            serviceOrderId = existing.id,
            complaint = "New complaint updated",
            diagnosis = existing.diagnosis,
            tasksIds = existing.tasks.map { it.id },
            inventoryItemsIds = existing.inventoryItems.map { it.id },
            status = existing.status
        )

        coEvery { serviceOrderRepository.read(existing.id) } returns existing
        coEvery { getTaskByListIdUseCase.execute(any()) } returns Result.success(existing.tasks)
        coEvery { getInventoryByListIdOrSkuUseCase.execute(any()) } returns Result.success(existing.inventoryItems)
        coEvery { serviceOrderRepository.update(any(), any()) } returns Unit

        impl.execute(command).getOrThrow()
        coVerify { serviceOrderRepository.update(any(), any()) }
    }
}

