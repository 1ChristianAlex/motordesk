package com.khrix.domain.serviceorder.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus

data class UpdateServiceOrderTaskCommand(
    val status: TaskProgressStatus,
    val serviceOrderId: Int,
    val taskId: Int,
)

interface UpdateServiceOrderTaskUseCase : BaseUseCase<UpdateServiceOrderTaskCommand, Unit>
