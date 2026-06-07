package com.khrix.infrastructure.exposed.serviceorder.database


import com.khrix.infrastructure.exposed.BaseTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass


object ServicesTable : BaseTable("services") {

    val name = varchar("name", 150)

    val description = text("description").nullable()

    val estimatedMinutes = integer("estimatedMinutes")

    val price = decimal("price", 12, 2)

    val isActive = bool("isActive").default(true)
}

class ServiceEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ServiceEntity>(ServicesTable)

    var name by ServicesTable.name
    var description by ServicesTable.description

    var estimatedMinutes by ServicesTable.estimatedMinutes

    var price by ServicesTable.price

    var isActive by ServicesTable.isActive

    var createdAt by ServicesTable.createdAt
    var updatedAt by ServicesTable.updatedAt
}