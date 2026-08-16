package com.khrix.infrastructure.redis.event.handler

import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.usecase.SendEmailUpdateUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import com.khrix.infrastructure.redis.event.RedisDataEvent
import com.khrix.infrastructure.redis.event.RedisDataEventHandler

class HandleEmailUpdateEvent(
    private val sendEmailUpdateUseCase: SendEmailUpdateUseCase,
    private val eventPublisher: EventPublisher,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        sendEmailUpdateUseCase.execute(payload).onFailure {
            if (it is SendEmailUseCaseError.Retry) {
                reschedulingEmailSent(payload)
            }
        }
    }

    override val eventKey: EmailEventKeys
        get() = EmailEventKeys.UPDATE_EVENT_NAME

    private suspend fun reschedulingEmailSent(payload: Int) {
        eventPublisher.publish(eventKey, payload)
    }

    override fun unwrapEvent(payload: String): RedisDataEvent<Int> = RedisDataEventHandler.unwrapEvent<Int>(payload)
}
