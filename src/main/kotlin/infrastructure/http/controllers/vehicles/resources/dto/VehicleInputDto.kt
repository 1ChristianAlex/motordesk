package com.khrix.infrastructure.http.controllers.vehicles.resources.dto

import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.domain.vehicle.model.Vehicle
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class VehicleInputDto(
    val plate: String,
    val brand: String,
    val model: String,
    val color: String,
    val year: Int,
    val mileage: Int = 0,
    val chassis: String? = null,
    val fuelType: FuelType,
) {
    fun toModel(userId: Int): Vehicle {
        return Vehicle(
            id = 0,
            userId = userId,
            plate = Plate(plate),
            brand = brand,
            model = model,
            color = color,
            year = Year(year),
            mileage = mileage,
            chassis = chassis,
            fuelType = fuelType,
        )
    }
}


@Serializable
data class VehicleOutputDto(
    val id: Int? = null,
    val userId: Int,
    val plate: String,
    val brand: String,
    val model: String,
    val color: String,
    val year: Int,
    val mileage: Int,
    val chassis: String,
    val fuelType: FuelType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
