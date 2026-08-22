package com.khrix.adapter.exposed.serviceorder.mapper

import com.khrix.adapter.exposed.serviceorder.database.ServiceOrderTasksTable
import com.khrix.adapter.exposed.serviceorder.database.TaskEntity
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import com.khrix.domain.valueobject.Price
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun TaskEntity.toModel(serviceOrderId: Int): Task =
    suspendTransaction {
        Task(
            id = this@toModel.id.value,
            name = name,
            description = description,
            estimatedMinutes = estimatedMinutes,
            price = Price(price),
            isActive = isActive,
            category = category,
            status =
                ServiceOrderTasksTable
                    .select(ServiceOrderTasksTable.status)
                    .where { ServiceOrderTasksTable.serviceOrder eq serviceOrderId }
                    .map { it[ServiceOrderTasksTable.status] }
                    .first(),
        )
    }

fun TaskEntity.toModel(): Task =
    Task(
        id = this@toModel.id.value,
        name = name,
        description = description,
        estimatedMinutes = estimatedMinutes,
        price = Price(price),
        isActive = isActive,
        category = category,
        status = TaskProgressStatus.NOT_STARTED,
    )
