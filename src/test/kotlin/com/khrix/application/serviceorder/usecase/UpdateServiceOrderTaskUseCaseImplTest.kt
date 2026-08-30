package com.khrix.application.serviceorder.usecase

import com.khrix.domain.serviceorder.port.repository.ServiceOrderRepository
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderTaskCommand
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UpdateServiceOrderTaskUseCaseImplTest {
    private val repository = mockk<ServiceOrderRepository>()
    private val useCase = UpdateServiceOrderTaskUseCaseImpl(repository)

    @Test
    fun `updates task progress in repository`() =
        runTest {
            val command = UpdateServiceOrderTaskCommand(TaskProgressStatus.IN_PROGRESS, 4, 7)
            coJustRun { repository.updateServiceOrderTask(4, 7, TaskProgressStatus.IN_PROGRESS) }
            useCase.execute(command).getOrThrow()
            coVerify(exactly = 1) { repository.updateServiceOrderTask(4, 7, TaskProgressStatus.IN_PROGRESS) }
        }
}
