package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import testutils.sampleTask

class GetTaskByIdUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = GetTaskByIdUseCaseImpl(taskRepository)

    @Test
    fun `internalExecute returns task when found`() {
        runBlocking {
            val task = sampleTask()
            coEvery { taskRepository.read(task.id) } returns task

            val res = impl.execute(task.id)
            assertEquals(task, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runBlocking {
            coEvery { taskRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}

