package com.khrix.infrastructure.exposed.serviceorder.database


import com.khrix.infrastructure.exposed.BaseTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass


object TaskTable : BaseTable("task") {

    val name = varchar("name", 150)

    val description = text("description").nullable()

    val estimatedMinutes = integer("estimatedMinutes")

    val price = decimal("price", 12, 2)

    val isActive = bool("isActive").default(true)
}

class TaskEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<TaskEntity>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description

    var estimatedMinutes by TaskTable.estimatedMinutes

    var price by TaskTable.price

    var isActive by TaskTable.isActive

    var createdAt by TaskTable.createdAt
    var updatedAt by TaskTable.updatedAt
}