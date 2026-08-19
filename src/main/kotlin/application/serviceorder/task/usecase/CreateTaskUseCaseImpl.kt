package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.CreateTaskUseCase

class CreateTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : BaseUseCaseImpl<Task, Task>(),
    CreateTaskUseCase {
    override suspend fun internalExecute(command: Task): Task = taskRepository.createRead(command)

    override suspend fun useCaseDescription(): String = "Create a new task for a service order"
}
