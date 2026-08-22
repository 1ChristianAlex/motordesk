package com.khrix.adapter.redis.event.handler

import com.khrix.application.email.publisher.EmailEventKeys
import com.khrix.domain.email.usecase.SendEmailApprovalUseCase

class HandleEmailApprovalEvent(
    private val sendEmailApprovalUseCase: SendEmailApprovalUseCase,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        sendEmailApprovalUseCase.execute(payload).getOrThrow()
    }

    override val eventKey: EmailEventKeys
        get() = EmailEventKeys.APPROVAL_EVENT_NAME
}
