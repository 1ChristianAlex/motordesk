package com.khrix.domain.history.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseRead
import com.khrix.domain.history.model.HistoryChanges

interface RegisterHistoryRepository :
    BaseRead<List<HistoryChanges>>,
    BaseCreate<HistoryChanges>
