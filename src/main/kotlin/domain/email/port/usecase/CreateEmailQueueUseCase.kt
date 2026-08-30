package com.khrix.domain.email.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder

interface CreateEmailQueueUseCase : BaseUseCase<ServiceOrder, Unit>
