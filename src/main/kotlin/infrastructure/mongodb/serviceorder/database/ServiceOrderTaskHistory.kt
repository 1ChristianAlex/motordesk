package com.khrix.infrastructure.mongodb.serviceorder.database

import com.khrix.domain.serviceorder.model.ServiceOrderTask
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlin.time.Clock
import kotlin.time.Instant

data class ServiceOrderTaskHistory(
    @BsonId
    val id: ObjectId,
    val taskId: Int,
    val status: TaskProgressStatus,
    val serviceOrderId: Int,
    val createdAt: Instant = Clock.System.now(),
) {
    companion object {
        fun fromModel(
            serviceOrderTask: ServiceOrderTask,
            id: ObjectId,
        ): ServiceOrderTaskHistory =
            ServiceOrderTaskHistory(
                status = serviceOrderTask.status,
                serviceOrderId = serviceOrderTask.serviceOrderId,
                taskId = serviceOrderTask.taskId,
                id = id,
            )
    }
}
