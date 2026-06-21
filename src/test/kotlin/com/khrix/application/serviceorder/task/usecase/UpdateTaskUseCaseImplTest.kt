package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import testutils.sampleTask
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = UpdateTaskUseCaseImpl(taskRepository)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `internalExecute updates task`() = runTest {
        val task = sampleTask()
        coEvery { taskRepository.update(task.id, task) } returns Unit

        impl.execute(task).getOrThrow()
        coVerify { taskRepository.update(task.id, task) }
    }
}

