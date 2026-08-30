package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.CreateTaskUseCase

class CreateTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : CreateTaskUseCase, BaseUseCaseImpl<Task, Task>() {
    override suspend fun internalExecute(command: Task): Task {
        return taskRepository.createRead(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Create a new task for a service order"
    }
}
