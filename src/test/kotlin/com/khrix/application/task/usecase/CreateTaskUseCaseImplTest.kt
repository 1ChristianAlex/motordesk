package com.khrix.application.task.usecase

import com.khrix.application.serviceorder.task.usecase.CreateTaskUseCaseImpl
import com.khrix.domain.serviceorder.task.repository.TaskRepository
import com.khrix.testutils.sampleTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun `internalExecute creates task`() =
        runTest {
            val task = sampleTask()
            coEvery { taskRepository.createRead(task) } returns task

            val res = impl.execute(task)
            assertEquals(task, res.getOrThrow())
            coVerify { taskRepository.createRead(task) }
        }
}
