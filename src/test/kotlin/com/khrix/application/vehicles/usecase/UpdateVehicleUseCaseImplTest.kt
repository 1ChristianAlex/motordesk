package com.khrix.application.vehicles.usecase

import com.khrix.domain.vehicle.repository.VehiclesRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import testutils.sampleVehicle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateVehicleUseCaseImplTest {
    private val repository = mockk<VehiclesRepository>()
    private val useCase = UpdateVehicleUseCaseImpl(repository)

    @Test
    fun `updates mutable vehicle fields from command`() = runTest {
        val existing = sampleVehicle().copy(color = "Black", mileage = 10)
        val command = existing.copy(color = "Blue", mileage = 20)
        coEvery { repository.read(existing.id) } returns existing
        coJustRun { repository.update(existing.id, any()) }
        useCase.execute(command).getOrThrow()
        coVerify { repository.update(existing.id, match { it.color == "Blue" && it.mileage == 20 }) }
    }

    @Test
    fun `fails when vehicle does not exist`() = runTest {
        val command = sampleVehicle(id = 99)
        coEvery { repository.read(99) } returns null
        val error = assertFailsWith<Exception> { useCase.execute(command).getOrThrow() }
        assertEquals("Could not find vehicle with id 99", error.message)
    }
}
