package com.khrix.domain.vehicle.model

import com.khrix.domain.user.model.Role
import com.khrix.domain.valueobject.toValidationError
import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import io.konform.validation.Validation
import kotlinx.datetime.LocalDateTime

enum class FuelType(val value: String) {
    GASOLINE("GASOLINE"),
    ETHANOL("ETHANOL"),
    FLEX("FLEX"),
    DIESEL("DIESEL"),
    ELECTRIC("ELECTRIC"),
    HYBRID("HYBRID");

    companion object {
        fun fromValue(value: String): FuelType {
            return entries.firstOrNull { it.value == value.uppercase() }
                ?: throw IllegalArgumentException("Invalid fuel type: $value")
        }
    }
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
) {
    private val validation = Validation.Companion<Vehicle> {
        Vehicle::brand {
            constrain("Brand cannot be blank") { it.isNotBlank() }
        }
        Vehicle::model {
            constrain("Model cannot be blank") { it.isNotBlank() }
        }
        Vehicle::color {
            constrain("Color cannot be blank") { it.isNotBlank() }
        }
        Vehicle::mileage {
            constrain("Mileage cannot be negative") { it >= 0 }
        }
        Vehicle::chassis {
            constrain("Chassis cannot be blank") { it.isNotBlank() }
        }
        Vehicle::userId {
            constrain("User ID cannot be blank") { it >= 0 }
        }
        Vehicle::fuelType {
            constrain("Fuel type cannot be blank") { it.value.isNotBlank() }
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }

    fun updateVehicle(vehicle: Vehicle, role: Role): Vehicle {
        if (vehicle.id != this.id) {
            throw IllegalArgumentException("Cannot update vehicle with different ID")
        }

        if (vehicle.userId != this.userId && role == Role.CLIENT) {
            throw IllegalArgumentException("Cannot update vehicle with different owner")
        }

        return this.copy(
            plate = vehicle.plate,
            brand = vehicle.brand,
            model = vehicle.model,
            color = vehicle.color,
            year = vehicle.year,
            mileage = vehicle.mileage,
            chassis = vehicle.chassis,
            fuelType = vehicle.fuelType
        )
    }
}