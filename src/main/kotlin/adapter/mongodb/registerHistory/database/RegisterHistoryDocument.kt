package com.khrix.adapter.mongodb.registerHistory.database

import com.khrix.domain.history.model.HistoryChanges
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant
import kotlin.time.toJavaInstant

data class RegisterHistoryDocument(
    @BsonId
    val id: ObjectId = ObjectId(),
    val registerId: Int,
    val changes: Map<String, String>,
    val changedAt: Instant = Instant.now(),
) {
    companion object {
        fun fromModel(history: HistoryChanges) =
            RegisterHistoryDocument(
                registerId = history.id,
                changes = history.changes,
                changedAt = history.changedAt.toJavaInstant(),
            )

        const val SERVICE_ORDER_HISTORY = "service_order_history"
        const val SERVICE_ORDER_TASK_HISTORY = "service_order_task_history"
    }
}
