package com.khrix.domain.serviceorder.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseRead
import com.khrix.domain.serviceorder.model.ServiceOrder

interface ServiceOrderHistoryRepository :
    BaseRead<List<ServiceOrder>>,
    BaseCreate<ServiceOrder>

