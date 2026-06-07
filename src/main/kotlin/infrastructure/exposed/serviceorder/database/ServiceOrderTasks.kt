package com.khrix.infrastructure.exposed.serviceorder.database

import org.jetbrains.exposed.v1.core.Table

object ServiceOrderTasksTable : Table(
    "serviceOrderTasks"
) {
    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val task =
        reference("taskId", TaskTable)

    override val primaryKey = PrimaryKey(serviceOrder, task)
}

