package com.khrix.adapter.inbound.http.controllers.vehicles.resources.dto

import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.domain.vehicle.model.Vehicle
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class VehicleInputDto(
    val id: Int = 0,
    val plate: String,
    val brand: String,
    val model: String,
    val color: String,
    val year: Int,
    val mileage: Int = 0,
    val chassis: String,
    val fuelType: FuelType,
    val ownerId: Int,
) {
    fun toModel(): Vehicle =
        Vehicle(
            id = id,
            ownerId = ownerId,
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

@Serializable
data class VehicleUpdateInputDto(
    val id: Int,
    val color: String?,
    val mileage: Int? = 0,
)

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
