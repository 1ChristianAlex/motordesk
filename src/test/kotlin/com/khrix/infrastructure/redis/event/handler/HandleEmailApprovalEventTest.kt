package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.usecase.SendEmailApprovalUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import com.khrix.infrastructure.redis.event.RedisDataEventHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class HandleEmailApprovalEventTest {
    private val sendEmailApprovalUseCase = mockk<SendEmailApprovalUseCase>()
    private val eventPublisher = mockk<EventPublisher>(relaxed = true)
    private val handler = HandleEmailApprovalEvent(sendEmailApprovalUseCase, eventPublisher)

    @Test
    fun `handles approval event by executing the send email use case`() =
        runTest {
            val payload = RedisDataEventHandler.wrapEvent(EmailEventKeys.APPROVAL_EVENT_NAME, 10)
            coEvery { sendEmailApprovalUseCase.execute(10) } returns Result.success(Unit)

            handler.handle(payload)

            coVerify(exactly = 1) { sendEmailApprovalUseCase.execute(10) }
        }

    @Test
    fun `re-publishes the event when send email asks for retry`() =
        runTest {
            val payload = RedisDataEventHandler.wrapEvent(EmailEventKeys.APPROVAL_EVENT_NAME, 10)
            coEvery { sendEmailApprovalUseCase.execute(10) } returns Result.failure(SendEmailUseCaseError.Retry())

            handler.handle(payload)

            coVerify(exactly = 1) { eventPublisher.publish(EmailEventKeys.APPROVAL_EVENT_NAME, 10) }
        }

    @Test
    fun `ignores events with different keys`() =
        runTest {
            val payload = RedisDataEventHandler.wrapEvent(EmailEventKeys.UPDATE_EVENT_NAME, 10)

            handler.handle(payload)

            coVerify(exactly = 0) { sendEmailApprovalUseCase.execute(any()) }
            coVerify(exactly = 0) { eventPublisher.publish(any(), any()) }
        }
}
