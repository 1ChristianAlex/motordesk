package com.khrix.application.vehicles.usecase

import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.domain.vehicle.repository.VehiclesRepository
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

class DeleteVehicleByIdUseCaseImplTest {
    private val serviceOrderRepository = mockk<ServiceOrderRepository>()
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = DeleteVehicleByIdUseCaseImpl(vehiclesRepository, serviceOrderRepository)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `internalExecute throws when vehicle not exists`() {
        runTest {
            coEvery { vehiclesRepository.read(1) } returns null

            val res = impl.execute(1)
            assertFailsWith<Exception> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute deletes when exists`() {
        runTest {
            val vehicle = testutils.sampleVehicle()
            coEvery { serviceOrderRepository.getOrderByVehicle(vehicle.id) } returns listOf()
            coEvery { vehiclesRepository.read(vehicle.id) } returns vehicle
            coEvery { vehiclesRepository.delete(vehicle.id) } returns Unit

            impl.execute(vehicle.id).getOrThrow()
            coVerify { vehiclesRepository.delete(vehicle.id) }
        }
    }
}


