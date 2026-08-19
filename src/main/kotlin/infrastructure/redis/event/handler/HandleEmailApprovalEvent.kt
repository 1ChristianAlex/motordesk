package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.usecase.SendEmailApprovalUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import com.khrix.infrastructure.redis.event.RedisDataEvent
import com.khrix.infrastructure.redis.event.RedisDataEventHandler

class HandleEmailApprovalEvent(
    private val sendEmailApprovalUseCase: SendEmailApprovalUseCase,
    private val eventPublisher: EventPublisher,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        sendEmailApprovalUseCase.execute(payload).onFailure {
            if (it is SendEmailUseCaseError.Retry) {
                reschedulingEmailSent(payload)
            }
        }
    }

    override val eventKey: EmailEventKeys
        get() = EmailEventKeys.APPROVAL_EVENT_NAME

    private suspend fun reschedulingEmailSent(payload: Int) {
        eventPublisher.publish(eventKey, payload)
    }

    override fun unwrapEvent(payload: String): RedisDataEvent<Int> = RedisDataEventHandler.unwrapEvent<Int>(payload)
}
