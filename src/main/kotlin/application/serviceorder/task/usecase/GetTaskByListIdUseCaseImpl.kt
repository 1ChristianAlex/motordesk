package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
import com.khrix.domain.serviceorder.task.port.usecase.GetTaskByListIdUseCase

class GetTaskByListIdUseCaseImpl(
    private val taskRepository: TaskRepository,
) : BaseUseCaseImpl<List<Int>, List<Task>>(),
    GetTaskByListIdUseCase {
    override suspend fun internalExecute(command: List<Int>): List<Task> = taskRepository.getTasks(command)

    override suspend fun useCaseDescription(): String = "Create a new task for a service order"
}
