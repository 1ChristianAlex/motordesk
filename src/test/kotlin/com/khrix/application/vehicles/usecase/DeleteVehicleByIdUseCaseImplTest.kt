package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.repository.VehiclesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteVehicleByIdUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = DeleteVehicleByIdUseCaseImpl(vehiclesRepository)

    @Test
    fun `internalExecute throws when vehicle not exists`() {
        runBlocking {
            coEvery { vehiclesRepository.read(1) } returns null

            val res = impl.execute(1)
            assertFailsWith<Exception> { res.getOrThrow() }
        }
    }

    @Test
    fun `internalExecute deletes when exists`() {
        runBlocking {
            val vehicle = testutils.sampleVehicle()
            coEvery { vehiclesRepository.read(vehicle.id) } returns vehicle
            coEvery { vehiclesRepository.delete(vehicle.id) } returns Unit

            impl.execute(vehicle.id).getOrThrow()
            coVerify { vehiclesRepository.delete(vehicle.id) }
        }
    }
}


