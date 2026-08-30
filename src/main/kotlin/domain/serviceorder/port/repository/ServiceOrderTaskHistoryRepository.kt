package com.khrix.domain.serviceorder.port.repository

import com.khrix.domain.core.BaseRead
import com.khrix.domain.serviceorder.model.ServiceOrderTask

interface ServiceOrderTaskHistoryRepository : BaseRead<List<ServiceOrderTask>> {
    suspend fun create(data: ServiceOrderTask)
}
