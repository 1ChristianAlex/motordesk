package com.khrix.application.task.usecase

import com.khrix.application.serviceorder.task.usecase.GetTaskByIdUseCaseImpl
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
import com.khrix.testutils.sampleTask
import io.mockk.coEvery
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
import kotlin.test.assertFailsWith

class GetTaskByIdUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = GetTaskByIdUseCaseImpl(taskRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns task when found`() {
        runTest {
            val task = sampleTask()
            coEvery { taskRepository.read(task.id) } returns task

            val res = impl.execute(task.id)
            assertEquals(task, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runTest {
            coEvery { taskRepository.read(2) } returns null
            val res = impl.execute(2)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}
