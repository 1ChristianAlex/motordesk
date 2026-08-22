package com.khrix.domain.history

import com.khrix.domain.history.model.HistoryChanges

interface DiffResolver<T> {
    fun shallowDiff(
        compareA: T,
        compareB: T,
    ): HistoryChanges
}
