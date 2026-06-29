package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.shortid.ShortId
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand
import com.khrix.domain.user.model.Role
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

class UpdateServiceOrderUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val getInventoryByListIdOrSkuUseCase = mockk<GetInventoryByListIdOrSkuUseCase>()
    private val getTaskByListIdUseCase = mockk<GetTaskByListIdUseCase>()
    private val shortId = mockk<ShortId>()
    private val serviceOrderHistoryRepository = mockk<ServiceOrderHistoryRepository>()
    private val createEmailQueueUseCase = mockk<CreateEmailQueueUseCase>()

    private val impl =
        UpdateServiceOrderUseCaseImpl(
            serviceOrderRepository,
            getInventoryByListIdOrSkuUseCase,
            getTaskByListIdUseCase,
            shortId,
            serviceOrderHistoryRepository,
            createEmailQueueUseCase,
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
    fun `internalExecute updates service order`() =
        runTest {
            val existing = sampleServiceOrder()
            val command =
                UpdateServiceOrderCommand(
                    code = "#order",
                    complaint = "New complaint updated",
                    diagnosis = existing.diagnosis,
                    tasksIds = existing.tasks.map { it.id },
                    inventoryItemsIds = existing.inventoryItems.map { it.id },
                    status = existing.status,
                    operatorRole = Role.MANAGER,
                )

            coEvery { serviceOrderRepository.getByCode(command.code) } returns existing
            coEvery { getTaskByListIdUseCase.execute(any()) } returns Result.success(existing.tasks)
            coEvery { getInventoryByListIdOrSkuUseCase.execute(any()) } returns Result.success(existing.inventoryItems)
            coEvery { serviceOrderRepository.update(any(), any()) } returns Unit
            coEvery { serviceOrderHistoryRepository.create(any()) } returns 1
            coEvery { createEmailQueueUseCase.execute(any()) } returns Result.success(Unit)
            coEvery { shortId.encode(any()) } returns "order"

            impl.execute(command).getOrThrow()
            coVerify { serviceOrderRepository.update(any(), any()) }
            coVerify { serviceOrderHistoryRepository.create(any()) }
        }
}
