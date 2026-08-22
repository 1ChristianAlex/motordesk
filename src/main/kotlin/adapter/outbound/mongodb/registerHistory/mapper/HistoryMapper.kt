package com.khrix.adapter.outbound.mongodb.registerHistory.mapper

import com.khrix.adapter.outbound.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.domain.history.model.HistoryChanges
import kotlin.time.toKotlinInstant

fun RegisterHistoryDocument.toModel(): HistoryChanges = HistoryChanges(this.registerId, this.changedAt.toKotlinInstant(), changes)
