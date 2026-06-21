package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleTask
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTaskByListIdUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = GetTaskByListIdUseCaseImpl(taskRepository)

    @Test
    fun `internalExecute returns tasks list`() = runBlocking {
        val tasks = listOf(sampleTask())
        coEvery { taskRepository.getTasks(listOf(1)) } returns tasks

        val res = impl.execute(listOf(1))
        assertEquals(tasks, res.getOrThrow())
    }
}

