package com.khrix.adapter.outbound.mongodb.registerHistory.mapper

import com.khrix.adapter.outbound.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.domain.history.model.HistoryChanges
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class HistoryMapperTest {
    @Test
    fun `maps Mongo history documents to and from the domain model`() {
        val history =
            HistoryChanges(
                id = 42,
                changedAt = Instant.parse("2025-03-08T12:34:56Z"),
                changes = mapOf("status" to "WAITING_APPROVAL"),
            )

        val document = RegisterHistoryDocument.fromModel(history)

        assertEquals(history.id, document.registerId)
        assertEquals(history, document.toModel())
    }
}
