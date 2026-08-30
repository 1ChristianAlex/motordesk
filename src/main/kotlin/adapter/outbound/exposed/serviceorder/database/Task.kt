package com.khrix.adapter.outbound.exposed.serviceorder.database

import com.khrix.adapter.outbound.exposed.BaseTable
import com.khrix.adapter.outbound.exposed.DatabaseSchemas
import com.khrix.domain.serviceorder.task.model.TaskCategory
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object TaskTable : BaseTable("task", DatabaseSchemas.SERVICE_ORDER) {
    val name = varchar("name", 150)

    val description = text("description").nullable()

    val estimatedMinutes = integer("estimatedMinutes")

    val price = decimal("price", 12, 2)

    val isActive = bool("isActive").default(true)

    val category = enumerationByName<TaskCategory>("category", 50)
}

class TaskEntity(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<TaskEntity>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description

    var estimatedMinutes by TaskTable.estimatedMinutes

    var price by TaskTable.price

    var isActive by TaskTable.isActive
    var category by TaskTable.category

    var createdAt by TaskTable.createdAt
    var updatedAt by TaskTable.updatedAt
}
