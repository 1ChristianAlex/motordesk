package com.khrix.domain.vehicle.model

import com.khrix.domain.valueobject.ValidationErrorResult
import testutils.sampleVehicle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FuelTypeTest {
    @Test
    fun `parses fuel type without case sensitivity`() {
        assertEquals(FuelType.HYBRID, FuelType.fromValue("hybrid"))
        assertFailsWith<IllegalArgumentException> { FuelType.fromValue("steam") }
    }
}

class VehicleTest {
    @Test
    fun `updates color and nondecreasing mileage`() {
        val vehicle = sampleVehicle().copy(mileage = 100)
        assertEquals("Blue", vehicle.updateColor("Blue").color)
        assertEquals(120, vehicle.updateMileage(120).mileage)
        assertFailsWith<IllegalArgumentException> { vehicle.updateMileage(99) }
    }

    @Test
    fun `rejects invalid vehicle data`() {
        assertFailsWith<ValidationErrorResult> { sampleVehicle().copy(brand = "") }
    }
}
