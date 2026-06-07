package com.khrix.infrastructure.exposed.serviceorder.repository

import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.serviceorder.database.TaskEntity
import com.khrix.infrastructure.exposed.serviceorder.database.TaskTable
import com.khrix.infrastructure.exposed.serviceorder.mapper.toModel
import org.jetbrains.exposed.v1.core.inList
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
                it.category = data.category

                if (!it.isActive) {
                    it.isActive = data.isActive
                }
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
            price = data.price.value
            isActive = data.isActive
            category = data.category
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery {
            TaskEntity.findByIdAndUpdate(id) {
                it.isActive = false
            }
        }
    }

    override suspend fun createRead(data: Task): Task {
        return createTask(data).toModel()
    }

    override suspend fun getTasks(ids: List<Int>): List<Task> {
        return suspendedQuery {
            TaskEntity.find { TaskTable.id inList ids }.map { it.toModel() }
        }
    }
}
