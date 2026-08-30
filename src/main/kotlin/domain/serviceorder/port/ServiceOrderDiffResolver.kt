package com.khrix.domain.serviceorder.port

import com.khrix.domain.history.port.DiffResolver
import com.khrix.domain.serviceorder.model.ServiceOrder

interface ServiceOrderDiffResolver : DiffResolver<ServiceOrder>
