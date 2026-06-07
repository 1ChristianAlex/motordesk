package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.UpdateTaskUseCase

class UpdateTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : UpdateTaskUseCase, BaseUseCaseImpl<Task, Unit>() {
    override suspend fun internalExecute(command: Task): Unit {
        return taskRepository.update(command.id, command)
    }

    override suspend fun useCaseDescription(): String {
        return "Update task by id"
    }
}
