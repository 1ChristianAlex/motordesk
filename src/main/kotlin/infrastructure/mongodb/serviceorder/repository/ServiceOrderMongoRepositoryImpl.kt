package com.khrix.infrastructure.mongodb.serviceorder.repository

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.infrastructure.mongodb.connection.MongoConnection
import com.khrix.infrastructure.mongodb.serviceorder.database.ServiceOrderHistory
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class ServiceOrderMongoRepositoryImpl(
    private val mongoConnection: MongoConnection,
    private val serviceOrderRepository: ServiceOrderRepository,
) : ServiceOrderHistoryRepository {
    private val collection by lazy {
        mongoConnection.database
            .getCollection<ServiceOrderHistory>(
                "service_order_history",
            )
    }

    override suspend fun read(id: Int): List<ServiceOrder> {
        val projectionFields =
            Projections.fields(
                Projections.include(
                    ServiceOrderHistory::status.name,
                    ServiceOrderHistory::complaint.name,
                    ServiceOrderHistory::diagnosis.name,
                ),
                Projections.excludeId(),
            )
        val sqlServiceOrder = serviceOrderRepository.read(id) ?: throw Exception()

        val documents =
            collection
                .find(
                    eq(
                        ServiceOrderHistory::serviceOrderId.name,
                        id,
                    ),
                ).projection(projectionFields)

        return documents
            .map {
                sqlServiceOrder.copy(
                    status = it.status,
                    complaint = it.complaint,
                    diagnosis = it.diagnosis,
                )
            }.toList()
    }

    override suspend fun create(data: ServiceOrder): Int {
        collection.insertOne(
            ServiceOrderHistory.fromModel(data, ObjectId()),
        )

        return data.id
    }
}
