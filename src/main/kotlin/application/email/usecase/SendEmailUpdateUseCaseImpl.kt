package com.khrix.application.email.usecase

import com.khrix.application.email.EmailSender
import com.khrix.application.email.toStatusUpdateEmail
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.email.usecase.SendEmailUpdateUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError

class SendEmailUpdateUseCaseImpl(
    private val emailSender: EmailSender,
    private val emailQueueRepository: EmailQueueRepository,
) : BaseUseCaseImpl<Int, Unit>(),
    SendEmailUpdateUseCase {
    override suspend fun internalExecute(command: Int) {
        var emailItem =
            emailQueueRepository.read(command)
                ?: throw SendEmailUseCaseError.NotFound()
        try {
            if (emailItem.shouldBeSend()) {
                emailItem = emailQueueRepository.registerAttempt(emailItem.id, EmailStatus.SENT)

                emailSender.send(
                    emailItem.toStatusUpdateEmail(),
                )
            }
        } catch (ex: Exception) {
            emailQueueRepository.setErrorMessage(emailItem.id, ex.message ?: "Failed to send email")
            if (emailItem.canRetry()) {
                throw SendEmailUseCaseError.Retry()
            } else {
                throw SendEmailUseCaseError.NoMoreRetriesAvailable()
            }
        }
    }

    override suspend fun useCaseDescription(): String = "Send email update for a given email queue item"
}
