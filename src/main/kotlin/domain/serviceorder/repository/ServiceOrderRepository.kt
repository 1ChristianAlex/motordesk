package com.khrix.domain.serviceorder.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.serviceorder.model.ServiceOrder

interface ServiceOrderRepository :
    BaseRead<ServiceOrder>,
    BaseUpdate<ServiceOrder>,
    BaseCreate<ServiceOrder>,
    BaseDelete,
    BaseCreateReturn<ServiceOrder> {
}


