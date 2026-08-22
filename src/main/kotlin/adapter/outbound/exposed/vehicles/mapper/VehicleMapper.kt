package com.khrix.adapter.outbound.exposed.vehicles.mapper

import com.khrix.adapter.outbound.exposed.vehicles.database.VehicleEntity
import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.vehicle.model.Vehicle

fun VehicleEntity.toModel(): Vehicle =
    Vehicle(
        id = id.value,
        ownerId = owner.id.value,
        plate = Plate(plate),
        brand = brand,
        model = model,
        color = color,
        year = Year(year),
        mileage = mileage,
        chassis = chassis,
        fuelType = fuelType,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
