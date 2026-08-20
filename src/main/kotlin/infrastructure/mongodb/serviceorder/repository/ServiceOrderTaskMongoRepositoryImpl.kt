package com.khrix.infrastructure.mongodb.serviceorder.repository

import com.khrix.domain.serviceorder.model.ServiceOrderTask
import com.khrix.domain.serviceorder.repository.ServiceOrderTaskHistoryRepository
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.infrastructure.mongodb.connection.MongoConnection
import com.khrix.infrastructure.mongodb.serviceorder.database.ServiceOrderTaskHistory
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class ServiceOrderTaskMongoRepositoryImpl(
    private val mongoConnection: MongoConnection,
    private val taskRepository: TaskRepository,
) : ServiceOrderTaskHistoryRepository {
    private val collection by lazy {
        mongoConnection.database
            .getCollection<ServiceOrderTaskHistory>(
                "service_order_task_history",
            )
    }

    override suspend fun read(id: Int): List<ServiceOrderTask> {
        val projectionFields =
            Projections.fields(
                Projections.include(
                    ServiceOrderTaskHistory::status.name,
                    ServiceOrderTaskHistory::serviceOrderId.name,
                    ServiceOrderTaskHistory::taskId.name,
                ),
                Projections.excludeId(),
            )

        val taskList = taskRepository.getTasksFromServiceOrder(id)

        val documents =
            collection
                .find(
                    eq(
                        ServiceOrderTaskHistory::serviceOrderId.name,
                        id,
                    ),
                ).projection(projectionFields)

        val historyTasks =
            documents
                .map { history ->
                    val taskItem = taskList.find { task -> task.id == history.taskId }

                    taskItem?.let {
                        ServiceOrderTask(id, taskItem.id, history.status)
                    }
                }.toList()
                .filterNotNull()

        return historyTasks
    }

    override suspend fun create(data: ServiceOrderTask) {
        collection.insertOne(
            ServiceOrderTaskHistory.fromModel(data),
        )
    }
}
