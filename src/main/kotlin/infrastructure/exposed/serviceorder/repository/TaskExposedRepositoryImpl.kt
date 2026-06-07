package com.khrix.infrastructure.exposed.serviceorder.repository

import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.serviceorder.database.TaskEntity
import com.khrix.infrastructure.exposed.serviceorder.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database

class TaskExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<TaskEntity, Task>(database), TaskRepository {
    override suspend fun read(id: Int): Task? {
        return suspendedQuery {
            TaskEntity.findById(id)?.toModel()
        }
    }

    override suspend fun update(id: Int, data: Task) {
        suspendedQuery {
            TaskEntity.findByIdAndUpdate(id) {
                it.name = data.name
                it.description = data.description
                it.estimatedMinutes = data.estimatedMinutes
                it.price = data.price
                it.isActive = data.isActive
            }
        }
    }

    override suspend fun create(data: Task): Int {
        return createTask(data).id.value
    }

    private suspend fun createTask(data: Task) = suspendedQuery {
        TaskEntity.new {
            name = data.name
            description = data.description
            estimatedMinutes = data.estimatedMinutes
            price = data.price
            isActive = data.isActive
        }
    }

    override suspend fun delete(id: Int) {
        this.update(
            id,
            read(id)?.copy(isActive = false) ?: throw IllegalArgumentException("Task with id $id not found")
        )
    }

    override suspend fun createRead(data: Task): Task {
        return createTask(data).toModel()
    }
}
