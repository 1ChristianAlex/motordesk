package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByIdUseCase

class GetTaskByIdUseCaseImpl(
    private val taskRepository: TaskRepository,
) : GetTaskByIdUseCase, BaseUseCaseImpl<Int, Task>() {
    override suspend fun internalExecute(command: Int): Task {
        return taskRepository.read(command) ?: throw NoSuchElementException("Task not found")
    }

    override suspend fun useCaseDescription(): String {
        return "Create a new task for a service order"
    }
}
