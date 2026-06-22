package com.khrix.infrastructure.mongodb.serviceorder.database

import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus

data class ServiceOrderTaskHistory(
    val taskId: Int,
    val status: TaskProgressStatus,
) {
    companion object {
        fun fromModel(task: Task): ServiceOrderTaskHistory {
            return ServiceOrderTaskHistory(
                taskId = task.id,
                status = task.status
            )
        }
    }
}