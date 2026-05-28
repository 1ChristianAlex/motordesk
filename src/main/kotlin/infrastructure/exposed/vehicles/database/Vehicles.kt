package com.khrix.infrastructure.exposed.vehicles.database

import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object VehiclesTable : BaseTable("vehicles") {
    val owner = reference("ownerId", UsersTable)
    val plate = varchar("plate", 10).uniqueIndex()
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val color = varchar("color", 50)
    val year = integer("year")
    val mileage = integer("mileage").default(0)
    val chassis = varchar("chassis", 30).nullable()
    val fuelType = varchar("fuelType", 30).nullable()
}

class VehicleEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<VehicleEntity>(VehiclesTable)

    var owner by UserEntity referencedOn VehiclesTable.owner

    var plate by VehiclesTable.plate

    var brand by VehiclesTable.brand
    var model by VehiclesTable.model

    var color by VehiclesTable.color

    var year by VehiclesTable.year

    var mileage by VehiclesTable.mileage

    var chassis by VehiclesTable.chassis

    var fuelType by VehiclesTable.fuelType

    var createdAt by VehiclesTable.createdAt
    var updatedAt by VehiclesTable.updatedAt
}