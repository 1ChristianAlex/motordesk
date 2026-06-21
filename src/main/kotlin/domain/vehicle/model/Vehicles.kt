package com.khrix.domain.vehicle.model

import com.khrix.domain.core.validateWith
import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import io.konform.validation.Validation
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

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

@Serializable
data class Vehicle(
    val id: Int = 0,
    val ownerId: Int,
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
        Vehicle::ownerId {
            constrain("User ID cannot be blank") { it >= 0 }
        }
        Vehicle::fuelType {
            constrain("Fuel type cannot be blank") { it.value.isNotBlank() }
        }
    }

    init {
        validateWith(validation)
    }

    fun updateVehicle(vehicle: Vehicle): Vehicle {
        if (vehicle.id != this.id) {
            throw IllegalArgumentException("Cannot update vehicle with different ID")
        }

        if (vehicle.ownerId != this.ownerId) {
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