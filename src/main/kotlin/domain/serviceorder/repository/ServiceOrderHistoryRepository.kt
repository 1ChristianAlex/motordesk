package com.khrix.domain.serviceorder.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.serviceorder.model.ServiceOrder

interface ServiceOrderHistoryRepository :
    BaseRead<List<ServiceOrder>>,
    BaseUpdate<ServiceOrder>,
    BaseCreate<ServiceOrder>,
    BaseDelete

