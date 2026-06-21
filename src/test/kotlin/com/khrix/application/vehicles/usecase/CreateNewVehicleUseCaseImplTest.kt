package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.repository.VehiclesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith
import testutils.sampleVehicle

class CreateNewVehicleUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = CreateNewVehicleUseCaseImpl(vehiclesRepository)

    @Test
    fun `internalExecute throws when existing vehicle found`() {
        runBlocking {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.getByPlateOrChassis(any(), any()) } returns vehicle

            val res = impl.execute(vehicle)
            assertFailsWith<Exception> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute creates when not exists`() {
        runBlocking {
            val vehicle = sampleVehicle()
            coEvery { vehiclesRepository.getByPlateOrChassis(any(), any()) } returns null
            coEvery { vehiclesRepository.createRead(vehicle) } returns vehicle

            val res = impl.execute(vehicle)
            val created = res.getOrThrow()
            kotlin.test.assertEquals(vehicle, created)
            coVerify { vehiclesRepository.createRead(vehicle) }
        }
    }
}


