package com.khrix.domain.serviceorder.model

import com.khrix.domain.serviceorder.task.model.TaskProgressStatus

data class ServiceOrderTask(
    val serviceOrderId: Int,
    val taskId: Int,
    val status: TaskProgressStatus,
)
