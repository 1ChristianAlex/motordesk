package com.khrix.adapter.outbound.mongodb.registerHistory.repository

import com.khrix.adapter.outbound.mongodb.connection.MongoConnection
import com.khrix.adapter.outbound.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.adapter.outbound.mongodb.registerHistory.mapper.toModel
import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.history.repository.RegisterHistoryRepository
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

abstract class RegisterHistoryRepositoryMongoFactory(
    private val mongoConnection: MongoConnection,
    private val collectionName: String,
) : RegisterHistoryRepository {
    private val collection by lazy {
        mongoConnection.database
            .getCollection<RegisterHistoryDocument>(
                collectionName,
            )
    }

    override suspend fun read(id: Int): List<HistoryChanges> {
        val projectionFields =
            Projections.fields(
                Projections.include(
                    RegisterHistoryDocument::changedAt.name,
                    RegisterHistoryDocument::changes.name,
                    RegisterHistoryDocument::registerId.name,
                ),
                Projections.excludeId(),
            )

        val documents =
            collection
                .find(
                    eq(
                        RegisterHistoryDocument::registerId.name,
                        id,
                    ),
                ).projection(projectionFields)

        return documents.map { it.toModel() }.toList()
    }

    override suspend fun create(data: HistoryChanges): Int {
        collection.insertOne(
            RegisterHistoryDocument.fromModel(data),
        )

        return data.id
    }
}
