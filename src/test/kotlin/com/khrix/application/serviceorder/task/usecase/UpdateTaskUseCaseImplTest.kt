package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleTask
import kotlin.test.Test

class UpdateTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = UpdateTaskUseCaseImpl(taskRepository)

    @Test
    fun `internalExecute updates task`() = runBlocking {
        val task = sampleTask()
        coEvery { taskRepository.update(task.id, task) } returns Unit

        impl.execute(task).getOrThrow()
        coVerify { taskRepository.update(task.id, task) }
    }
}

