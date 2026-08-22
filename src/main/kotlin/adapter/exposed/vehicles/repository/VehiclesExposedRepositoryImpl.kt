package com.khrix.adapter.exposed.vehicles.repository

import com.khrix.adapter.exposed.BaseExposedRepository
import com.khrix.adapter.exposed.user.database.UserEntity
import com.khrix.adapter.exposed.vehicles.database.VehicleEntity
import com.khrix.adapter.exposed.vehicles.database.VehicleTable
import com.khrix.adapter.exposed.vehicles.mapper.toModel
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.vehicle.repository.VehiclesRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.Database

class VehiclesExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<VehicleEntity, Vehicle>(database),
    VehiclesRepository {
    override suspend fun read(id: Int): Vehicle? =
        suspendedQuery {
            VehicleEntity.findById(id)?.toModel()
        }

    override suspend fun update(
        id: Int,
        data: Vehicle,
    ) {
        suspendedQuery {
            VehicleEntity.findByIdAndUpdate(id) {
                it.plate = data.plate.value
                it.brand = data.brand
                it.model = data.model
                it.color = data.color
                it.year = data.year.value
                it.mileage = data.mileage
                it.chassis = data.chassis
                it.fuelType = data.fuelType
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { VehicleEntity[id].delete() }
    }

    private fun createNewVehicle(data: Vehicle): VehicleEntity =
        VehicleEntity.new {
            plate = data.plate.value
            brand = data.brand
            model = data.model
            color = data.color
            year = data.year.value
            mileage = data.mileage
            chassis = data.chassis
            fuelType = data.fuelType
            owner = UserEntity[data.ownerId]
        }

    override suspend fun createRead(data: Vehicle): Vehicle = suspendedQuery { createNewVehicle(data).toModel() }

    override suspend fun getVehicleByOwnerId(id: Int): List<Vehicle> =
        suspendedQuery {
            VehicleEntity.find { VehicleTable.owner eq id }.map { it.toModel() }
        }

    override suspend fun getByPlateOrChassis(
        plate: String,
        chassis: String,
    ): Vehicle? =
        suspendedQuery {
            VehicleEntity
                .find { (VehicleTable.plate eq plate) or (VehicleTable.chassis eq chassis) }
                .firstOrNull()
                ?.toModel()
        }
}
