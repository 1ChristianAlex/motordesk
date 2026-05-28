package com.khrix.domain.vehicle

import kotlinx.datetime.LocalDateTime

enum class FuelType {
    GASOLINE,
    ETHANOL,
    FLEX,
    DIESEL,
    ELECTRIC,
    HYBRID
}

data class Vehicle(
    val id: Int? = null,
    val ownerId: Int,
    val plate: String,
    val brand: String,
    val model: String,
    val color: String,
    val year: Int,
    val mileage: Int = 0,
    val chassis: String? = null,
    val fuelType: FuelType? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)