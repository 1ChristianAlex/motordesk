package com.khrix.infrastructure.mongodb.registerHistory.mapper

import com.khrix.domain.history.model.HistoryChanges
import com.khrix.infrastructure.mongodb.registerHistory.database.RegisterHistoryDocument
import kotlin.time.toKotlinInstant

fun RegisterHistoryDocument.toModel(): HistoryChanges = HistoryChanges(this.registerId, this.changedAt.toKotlinInstant(), changes)
