package com.khrix.application.task.usecase

import com.khrix.application.serviceorder.task.usecase.DeleteTaskUseCaseImpl
import com.khrix.domain.serviceorder.task.port.repository.TaskRepository
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

class DeleteTaskUseCaseImplTest {
    private val taskRepository = mockk<TaskRepository>()
    private val impl = DeleteTaskUseCaseImpl(taskRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute calls delete on repository`() =
        runTest {
            coEvery { taskRepository.delete(1) } returns Unit

            impl.execute(1).getOrThrow()
            coVerify { taskRepository.delete(1) }
        }
}
