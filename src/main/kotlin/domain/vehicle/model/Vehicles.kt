package com.khrix.domain.vehicle.model

import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import kotlinx.datetime.LocalDateTime

enum class FuelType(val value: String) {
    GASOLINE("GASOLINE"),
    ETHANOL("ETHANOL"),
    FLEX("FLEX"),
    DIESEL("DIESEL"),
    ELECTRIC("ELECTRIC"),
    HYBRID("HYBRID")
}

data class Vehicle(
    val id: Int? = null,
    val userId: Int,
    val plate: Plate,
    val brand: String,
    val model: String,
    val color: String,
    val year: Year,
    val mileage: Int = 0,
    val chassis: String,
    val fuelType: FuelType,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)