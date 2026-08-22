package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.port.repository.VehiclesRepository
import com.khrix.testutils.sampleVehicle
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateNewVehicleUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = CreateNewVehicleUseCaseImpl(vehiclesRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute throws when existing vehicle found`() {
        runTest {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.getByPlateOrChassis(any(), any()) } returns vehicle

            val res = impl.execute(vehicle)
            assertFailsWith<Exception> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute creates when not exists`() {
        runTest {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.getByPlateOrChassis(any(), any()) } returns null
            coEvery { vehiclesRepository.createRead(vehicle) } returns vehicle

            val res = impl.execute(vehicle)
            val created = res.getOrThrow()
            assertEquals(vehicle, created)
            coVerify { vehiclesRepository.createRead(vehicle) }
        }
    }
}
