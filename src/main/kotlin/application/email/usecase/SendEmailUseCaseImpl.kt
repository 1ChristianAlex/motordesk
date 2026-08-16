package com.khrix.application.email.usecase

import com.khrix.application.email.EmailSender
import com.khrix.application.email.toApprovalEmail
import com.khrix.application.serviceorder.ApprovalLinkGenerator
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.email.usecase.SendEmailUseCase
import com.khrix.domain.email.usecase.SendEmailUseCaseError
import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken
import com.khrix.domain.serviceorder.repository.ServiceOrderApprovalRepository
import com.khrix.domain.user.security.SecurityHasher

class SendEmailUseCaseImpl(
    private val emailSender: EmailSender,
    private val emailQueueRepository: EmailQueueRepository,
    private val serviceOrderApprovalRepository: ServiceOrderApprovalRepository,
    private val securityHasher: SecurityHasher,
    private val approvalLinkGenerator: ApprovalLinkGenerator,
) : BaseUseCaseImpl<Int, Unit>(),
    SendEmailUseCase {
    override suspend fun internalExecute(command: Int) {
        var emailItem =
            emailQueueRepository.read(command)
                ?: throw SendEmailUseCaseError.NotFound()
        try {
            if (emailItem.shouldBeSend()) {
                val token =
                    serviceOrderApprovalRepository.createRead(
                        ServiceOrderApprovalToken(
                            emailItem.orderCode,
                            securityHasher,
                        ),
                    )

                emailItem = emailQueueRepository.registerAttempt(emailItem.id, EmailStatus.SENT)
                emailSender.send(
                    emailItem.toApprovalEmail(
                        approvalLinkGenerator.generate(token.tokenHash, emailItem.orderCode),
                        true,
                    ),
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

    override suspend fun useCaseDescription(): String = "Send email for a given email queue item"
}
