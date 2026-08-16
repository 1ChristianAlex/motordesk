package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.DeleteTaskUseCase

class DeleteTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : BaseUseCaseImpl<Int, Unit>(),
    DeleteTaskUseCase {
    override suspend fun internalExecute(command: Int) {
        taskRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String = "Soft delete task - deactivate"
}
