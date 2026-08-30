package com.khrix.domain.serviceorder.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

interface GetServiceOrdersByClientIdUseCase : BaseUseCase<Int, List<ServiceOrder>>
