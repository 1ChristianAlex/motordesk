package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.domain.serviceorder.task.usecase.GetTaskByListIdUseCase

class GetTaskByListIdUseCaseImpl(
    private val taskRepository: TaskRepository,
) : GetTaskByListIdUseCase, BaseUseCaseImpl<List<Int>, List<Task>>() {
    override suspend fun internalExecute(command: List<Int>): List<Task> {
        return taskRepository.getTasks(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Create a new task for a service order"
    }
}
