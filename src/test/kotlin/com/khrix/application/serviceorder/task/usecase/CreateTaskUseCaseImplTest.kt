package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleTask
import kotlin.test.Test

class CreateTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = CreateTaskUseCaseImpl(taskRepository)

    @Test
    fun `internalExecute creates task`() = runBlocking {
        val task = sampleTask()
        coEvery { taskRepository.createRead(task) } returns task

        val res = impl.execute(task)
        kotlin.test.assertEquals(task, res.getOrThrow())
        coVerify { taskRepository.createRead(task) }
    }
}

