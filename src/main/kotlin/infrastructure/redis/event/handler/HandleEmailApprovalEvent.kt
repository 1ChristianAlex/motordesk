package com.khrix.infrastructure.redis.event.handler

import com.khrix.application.notification.EmailSender
import com.khrix.application.notification.toApprovalEmail
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.publisher.EventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.infrastructure.redis.event.RedisDataEvent
import com.khrix.infrastructure.redis.event.RedisDataEventHandler

class HandleEmailApprovalEvent(
    private val emailSender: EmailSender,
    private val emailQueueRepository: EmailQueueRepository,
    private val eventPublisher: EventPublisher,
) : HandleConsumerEvent<Int>() {
    override suspend fun internalHandler(payload: Int) {
        runCatching {
            trySendEmail(payload)
        }.onFailure {
            if (it is HandleConsumerEventError.Retry) {
                reschedulingEmailSent(payload)
            }
        }
    }

    private suspend fun reschedulingEmailSent(payload: Int) {
        eventPublisher.publish(EventKeys.APPROVAL_EVENT_NAME, payload)
    }

    private suspend fun trySendEmail(payload: Int) {
        var emailItem =
            emailQueueRepository.read(payload)
                ?: throw HandleConsumerEventError.NotFound()
        try {
            if (emailItem.shouldBeSend()) {
                emailSender.send(emailItem.toApprovalEmail())
                emailItem = emailQueueRepository.registerAttempt(emailItem.id, EmailStatus.SENT)
            }
        } catch (ex: Exception) {
            emailQueueRepository.setErrorMessage(emailItem.id, ex.message ?: "Failed to send email")
            if (emailItem.canRetry()) {
                throw HandleConsumerEventError.Retry()
            } else {
                throw HandleConsumerEventError.NoMoreRetriesAvailable()
            }
        }
    }

    override val eventKey: EventKeys
        get() = EventKeys.APPROVAL_EVENT_NAME

    override fun unwrapEvent(payload: String): RedisDataEvent<Int> = RedisDataEventHandler.unwrapEvent<Int>(payload)
}
