package com.khrix.adapter.outbound.redis.event.handler

import com.khrix.adapter.outbound.redis.event.RedisDataEvent
import com.khrix.adapter.outbound.redis.event.handler.HandleEmailApprovalEvent
import com.khrix.application.email.publisher.EmailEventKeys
import com.khrix.domain.email.usecase.SendEmailApprovalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class HandleEmailApprovalEventTest {
    private val sendEmailApprovalUseCase = mockk<SendEmailApprovalUseCase>()
    private val handler = HandleEmailApprovalEvent(sendEmailApprovalUseCase)

    @Test
    fun `handles approval event by executing the send email use case`() =
        runTest {
            val payload = RedisDataEvent(EmailEventKeys.APPROVAL_EVENT_NAME, 10)
            coEvery { sendEmailApprovalUseCase.execute(10) } returns Result.success(Unit)

            handler.handle(payload)

            coVerify(exactly = 1) { sendEmailApprovalUseCase.execute(10) }
        }

    @Test
    fun `propagates failures from the send email use case`() =
        runTest {
            val payload = RedisDataEvent(EmailEventKeys.APPROVAL_EVENT_NAME, 10)
            coEvery { sendEmailApprovalUseCase.execute(10) } returns Result.failure(IllegalStateException("boom"))

            assertFailsWith<IllegalStateException> {
                handler.handle(payload)
            }

            coVerify(exactly = 1) { sendEmailApprovalUseCase.execute(10) }
        }

    @Test
    fun `ignores events with different keys`() =
        runTest {
            val payload = RedisDataEvent(EmailEventKeys.UPDATE_EVENT_NAME, 10)

            handler.handle(payload)

            coVerify(exactly = 0) { sendEmailApprovalUseCase.execute(any()) }
        }
}
