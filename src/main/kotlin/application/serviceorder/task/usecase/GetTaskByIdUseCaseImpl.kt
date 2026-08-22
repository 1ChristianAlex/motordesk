package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
import com.khrix.domain.serviceorder.task.port.usecase.GetTaskByIdUseCase

class GetTaskByIdUseCaseImpl(
    private val taskRepository: TaskRepository,
) : BaseUseCaseImpl<Int, Task>(),
    GetTaskByIdUseCase {
    override suspend fun internalExecute(command: Int): Task =
        taskRepository.read(command) ?: throw NoSuchElementException("Task not found")

    override suspend fun useCaseDescription(): String = "Create a new task for a service order"
}
