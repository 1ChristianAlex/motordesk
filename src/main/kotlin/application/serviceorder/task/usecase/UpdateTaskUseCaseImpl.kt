package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
import com.khrix.domain.serviceorder.task.port.usecase.UpdateTaskUseCase

class UpdateTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : BaseUseCaseImpl<Task, Unit>(),
    UpdateTaskUseCase {
    override suspend fun internalExecute(command: Task) = taskRepository.update(command.id, command)

    override suspend fun useCaseDescription(): String = "Update task by id"
}
