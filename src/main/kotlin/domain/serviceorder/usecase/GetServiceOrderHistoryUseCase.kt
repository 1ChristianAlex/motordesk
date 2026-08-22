package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.history.model.HistoryChanges

interface GetServiceOrderHistoryUseCase : BaseUseCase<Int, List<HistoryChanges>>
