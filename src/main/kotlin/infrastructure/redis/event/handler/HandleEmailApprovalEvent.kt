package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.usecase.SendEmailUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import com.khrix.infrastructure.redis.event.RedisDataEvent
import com.khrix.infrastructure.redis.event.RedisDataEventHandler

class HandleEmailApprovalEvent(
    private val sendEmailUseCase: SendEmailUseCase,
    private val eventPublisher: EventPublisher,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        sendEmailUseCase.execute(payload).onFailure {
            if (it is SendEmailUseCaseError.Retry) {
                reschedulingEmailSent(payload)
            }
        }
    }

    private suspend fun reschedulingEmailSent(payload: Int) {
        eventPublisher.publish(EmailEventKeys.APPROVAL_EVENT_NAME, payload)
    }

    override val eventKey: EmailEventKeys
        get() = EmailEventKeys.APPROVAL_EVENT_NAME

    override fun unwrapEvent(payload: String): RedisDataEvent<Int> = RedisDataEventHandler.unwrapEvent<Int>(payload)
}
