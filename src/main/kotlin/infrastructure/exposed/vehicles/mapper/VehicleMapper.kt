package com.khrix.infrastructure.exposed.vehicles.mapper

import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity

fun VehicleEntity.toModel(): Vehicle {
    return Vehicle(
        id = id.value,
        userId = owner.id.value,
        plate = Plate(plate),
        brand = brand,
        model = model,
        color = color,
        year = Year(year),
        mileage = mileage,
        chassis = chassis,
        fuelType = FuelType.valueOf(fuelType),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}