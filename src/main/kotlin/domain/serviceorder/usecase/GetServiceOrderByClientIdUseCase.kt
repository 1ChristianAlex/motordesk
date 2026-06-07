package com.khrix.domain.serviceorder.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

interface GetServiceOrderByClientIdUseCase : BaseUseCase<Int, ServiceOrder>
