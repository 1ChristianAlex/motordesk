package com.khrix.infrastructure.mongodb.serviceorder.database

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class ServiceOrderHistory(
    @BsonId
    val id: ObjectId = ObjectId(),
    val serviceOrderId: Int,
    val status: ServiceOrderStatus,
    val complaint: String? = null,
    val diagnosis: String? = null,
    val createdAt: Instant = Instant.now(),
) {
    companion object {
        fun fromModel(serviceOrder: ServiceOrder): ServiceOrderHistory =
            ServiceOrderHistory(
                status = serviceOrder.status,
                complaint = serviceOrder.complaint,
                diagnosis = serviceOrder.diagnosis,
                serviceOrderId = serviceOrder.id,
            )
    }
}
