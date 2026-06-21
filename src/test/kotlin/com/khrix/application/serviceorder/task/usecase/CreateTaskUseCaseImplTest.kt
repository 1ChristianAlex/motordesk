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

class CreateTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = CreateTaskUseCaseImpl(taskRepository)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `internalExecute creates task`() = runTest {
        val task = sampleTask()
        coEvery { taskRepository.createRead(task) } returns task

        val res = impl.execute(task)
        kotlin.test.assertEquals(task, res.getOrThrow())
        coVerify { taskRepository.createRead(task) }
    }
}

