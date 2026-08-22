package com.khrix.adapter.mongodb.registerHistory.mapper

import com.khrix.adapter.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.domain.history.model.HistoryChanges
import kotlin.time.toKotlinInstant

fun RegisterHistoryDocument.toModel(): HistoryChanges = HistoryChanges(this.registerId, this.changedAt.toKotlinInstant(), changes)
