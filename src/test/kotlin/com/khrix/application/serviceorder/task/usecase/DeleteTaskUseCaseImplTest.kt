package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class DeleteTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = DeleteTaskUseCaseImpl(taskRepository)

    @Test
    fun `internalExecute calls delete on repository`() = runBlocking {
        coEvery { taskRepository.delete(1) } returns Unit

        impl.execute(1).getOrThrow()
        coVerify { taskRepository.delete(1) }
    }
}

