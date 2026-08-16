package com.khrix.domain.email.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.model.ServiceOrderStatus

data class UpdateEmailQueueCommand(
    val serviceOrderStatus: ServiceOrderStatus,
    val code: String,
)

interface UpdateEmailQueueUseCase : BaseUseCase<UpdateEmailQueueCommand, Unit>
