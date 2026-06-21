package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.repository.VehiclesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import testutils.sampleVehicle
import kotlin.test.Test
import kotlin.test.assertEquals

class GetVehicleByOwnerIdUseCaseImplTest {
    private val vehiclesRepository = mockk<VehiclesRepository>()
    private val impl = GetVehicleByOwnerIdUseCaseImpl(vehiclesRepository)

    @Test
    fun `internalExecute returns list of vehicles`() = runTest {
        val vehicles = listOf(sampleVehicle())
        coEvery { vehiclesRepository.getVehicleByOwnerId(1) } returns vehicles

        val res = impl.execute(1)
        assertEquals(vehicles, res.getOrThrow())
    }
}

