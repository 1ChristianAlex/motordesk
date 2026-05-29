package com.khrix.infrastructure.exposed.vehicles.repository

import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity
import com.khrix.infrastructure.exposed.vehicles.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database

class VehiclesExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<VehicleEntity, Vehicle>(database), VehiclesRepository {
    override suspend fun read(id: Int): Vehicle {
        return suspendedQuery {
            VehicleEntity[id].toModel()
        }
    }

    override suspend fun update(id: Int, data: Vehicle) {
        suspendedQuery {
            VehicleEntity.findByIdAndUpdate(id) {
                it.plate = data.plate.value
                it.brand = data.brand
                it.model = data.model
                it.color = data.color
                it.year = data.year.value
                it.mileage = data.mileage
                it.chassis = data.chassis
                it.fuelType = data.fuelType.value
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { VehicleEntity[id].delete() }
    }

    private fun createNewVehicle(data: Vehicle): VehicleEntity {
        return VehicleEntity.new {
            plate = data.plate.value
            brand = data.brand
            model = data.model
            color = data.color
            year = data.year.value
            mileage = data.mileage
            chassis = data.chassis
            fuelType = data.fuelType.value
            owner = UserEntity[data.userId]
        }
    }

    override suspend fun createRead(data: Vehicle): Vehicle {
        return suspendedQuery { createNewVehicle(data).toModel() }
    }
}