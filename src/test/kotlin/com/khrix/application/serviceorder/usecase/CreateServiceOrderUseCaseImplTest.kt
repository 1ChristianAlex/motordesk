package com.khrix.application.serviceorder.usecase

import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.vehicle.usecase.GetVehicleByIdUseCase
import com.khrix.infrastructure.sqids.SqIdsShortIdImpl
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
import kotlin.test.assertEquals

class CreateServiceOrderUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val getInventoryByListIdOrSkuUseCase = mockk<GetInventoryByListIdOrSkuUseCase>()
    private val getUserUseCase = mockk<GetUserUseCase>()
    private val getVehicleByIdUseCase = mockk<GetVehicleByIdUseCase>()
    private val getTaskByListIdUseCase = mockk<GetTaskByListIdUseCase>()
    private val createEmailQueueUseCase = mockk<CreateEmailQueueUseCase>()
    private val serviceOrderHistoryRepository = mockk<ServiceOrderHistoryRepository>()

    private val impl = CreateServiceOrderUseCaseImpl(
        serviceOrderRepository,
        getInventoryByListIdOrSkuUseCase,
        getUserUseCase,
        getVehicleByIdUseCase,
        getTaskByListIdUseCase,
        createEmailQueueUseCase,
        shortId = SqIdsShortIdImpl(),
        serviceOrderHistoryRepository = serviceOrderHistoryRepository
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
    fun `internalExecute creates service order and enqueues email`() = runTest {
        val sample = sampleServiceOrder()
        val command = com.khrix.domain.serviceorder.usecase.CreateServiceOrderCommand(
            clientId = sample.client.id,
            operatorId = sample.operator.id,
            vehicleId = sample.vehicle.id,
            complaint = sample.complaint,
            diagnosis = sample.diagnosis,
            tasksIds = sample.tasks.map { it.id },
            inventoryItemsIds = sample.inventoryItems.map { it.id }
        )

        coEvery { getUserUseCase.execute(sample.client.id) } returns Result.success(sample.client)
        coEvery { getUserUseCase.execute(sample.operator.id) } returns Result.success(sample.operator)
        coEvery { getVehicleByIdUseCase.execute(sample.vehicle.id) } returns Result.success(sample.vehicle)
        coEvery { getTaskByListIdUseCase.execute(any()) } returns Result.success(sample.tasks)
        coEvery { getInventoryByListIdOrSkuUseCase.execute(any()) } returns Result.success(sample.inventoryItems)
        coEvery { serviceOrderRepository.createRead(any()) } returns sample
        coEvery { serviceOrderRepository.getByCode(any()) } returns null
        coEvery { createEmailQueueUseCase.execute(any()) } returns Result.success(Unit)
        coEvery { serviceOrderHistoryRepository.create(any()) } returns 1

        val res = impl.execute(command)
        val created = res.getOrThrow()
        assertEquals(sample, created)
        coVerify { serviceOrderRepository.createRead(any()) }
        coVerify { createEmailQueueUseCase.execute(any()) }
        coVerify { serviceOrderHistoryRepository.create(any()) }
    }
}

