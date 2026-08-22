package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.port.repository.VehiclesRepository
import com.khrix.testutils.sampleVehicle
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
import kotlin.test.assertFailsWith

class GetVehicleByIdUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = GetVehicleByIdUseCaseImpl(vehiclesRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns vehicle when found`() {
        runTest {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.read(1) } returns vehicle

            val res = impl.execute(1)
            assertEquals(vehicle, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runTest {
            coEvery { vehiclesRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}
