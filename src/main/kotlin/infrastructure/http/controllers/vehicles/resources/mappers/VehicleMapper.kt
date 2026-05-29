package com.khrix.infrastructure.http.controllers.vehicles.resources.mappers

import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleOutputDto

fun Vehicle.toOutputDto(): VehicleOutputDto {
    return VehicleOutputDto(
        id = id,
        userId = userId,
        plate = plate.value,
        brand = brand,
        model = model,
        color = color,
        year = year.value,
        mileage = mileage,
        chassis = chassis,
        fuelType = fuelType,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
    )
}