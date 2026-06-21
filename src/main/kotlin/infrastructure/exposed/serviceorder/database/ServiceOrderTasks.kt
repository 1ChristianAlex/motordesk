package com.khrix.infrastructure.exposed.serviceorder.database

import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import org.jetbrains.exposed.v1.core.Table

object ServiceOrderTasksTable : Table(
    "serviceOrderTasks"
) {
    val serviceOrder =
        reference("serviceOrderId", ServiceOrdersTable)

    val task =
        reference("taskId", TaskTable)

    val status = enumerationByName<TaskProgressStatus>("status", 50).default(TaskProgressStatus.NOT_STARTED)

    override val primaryKey = PrimaryKey(serviceOrder, task)
}
