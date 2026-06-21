package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.repository.VehiclesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import testutils.sampleVehicle

class GetVehicleByIdUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = GetVehicleByIdUseCaseImpl(vehiclesRepository)

    @Test
    fun `internalExecute returns vehicle when found`() {
        runBlocking {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.read(1) } returns vehicle

            val res = impl.execute(1)
            assertEquals(vehicle, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runBlocking {
            coEvery { vehiclesRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}


