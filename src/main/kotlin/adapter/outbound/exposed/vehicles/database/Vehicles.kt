package com.khrix.adapter.outbound.exposed.vehicles.database

import com.khrix.adapter.outbound.exposed.BaseTable
import com.khrix.adapter.outbound.exposed.DatabaseSchemas
import com.khrix.adapter.outbound.exposed.user.database.UserEntity
import com.khrix.adapter.outbound.exposed.user.database.UsersTable
import com.khrix.domain.vehicle.model.FuelType
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object VehicleTable : BaseTable("vehicles", DatabaseSchemas.VEHICLES) {
    val owner = reference("ownerId", UsersTable)
    val plate = varchar("plate", 7).uniqueIndex()
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val color = varchar("color", 50)
    val year = integer("year")
    val mileage = integer("mileage").default(0)
    val chassis = varchar("chassis", 30).uniqueIndex()
    val fuelType = enumerationByName<FuelType>("fuelType", 20)
}

class VehicleEntity(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<VehicleEntity>(VehicleTable)

    var owner by UserEntity referencedOn VehicleTable.owner

    var plate by VehicleTable.plate

    var brand by VehicleTable.brand
    var model by VehicleTable.model

    var color by VehicleTable.color

    var year by VehicleTable.year

    var mileage by VehicleTable.mileage

    var chassis by VehicleTable.chassis

    var fuelType by VehicleTable.fuelType

    var createdAt by VehicleTable.createdAt
    var updatedAt by VehicleTable.updatedAt
}
