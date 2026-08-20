package com.khrix.domain.history.model

import kotlin.time.Instant

data class HistoryChanges(
    val id: Int,
    val changedAt: Instant,
    val changes: List<RegisterChange<Comparable<String>>>,
)
