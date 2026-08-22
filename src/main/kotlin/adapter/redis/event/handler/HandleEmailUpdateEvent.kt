package com.khrix.adapter.redis.event.handler

import com.khrix.application.email.publisher.EmailEventKeys
import com.khrix.domain.email.usecase.SendEmailUpdateUseCase

class HandleEmailUpdateEvent(
    private val sendEmailUpdateUseCase: SendEmailUpdateUseCase,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        sendEmailUpdateUseCase.execute(payload).getOrThrow()
    }

    override val eventKey: EmailEventKeys
        get() = EmailEventKeys.UPDATE_EVENT_NAME
}
