package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.DeleteTaskUseCase

class DeleteTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : DeleteTaskUseCase, BaseUseCaseImpl<Int, Unit>() {
    override suspend fun internalExecute(command: Int) {
        taskRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Soft delete task - deactivate"
    }
}
