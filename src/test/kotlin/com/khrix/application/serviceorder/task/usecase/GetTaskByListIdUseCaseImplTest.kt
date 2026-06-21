package com.khrix.application.serviceorder.task.usecase

import com.khrix.domain.serviceorder.task.repository.TaskRepository
import io.mockk.coEvery
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
import kotlin.test.assertEquals

class GetTaskByListIdUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = GetTaskByListIdUseCaseImpl(taskRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns tasks list`() = runTest {
        val tasks = listOf(sampleTask())
        coEvery { taskRepository.getTasks(listOf(1)) } returns tasks

        val res = impl.execute(listOf(1))
        assertEquals(tasks, res.getOrThrow())
    }
}

