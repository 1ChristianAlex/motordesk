package com.khrix.application.email.usecase

import com.khrix.application.email.EmailSender
import com.khrix.application.email.toApprovalEmail
import com.khrix.application.serviceorder.ApprovalLinkGenerator
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.port.repository.EmailQueueRepository
import com.khrix.domain.email.port.usecase.SendEmailApprovalUseCase
import com.khrix.domain.email.port.usecase.SendEmailUseCaseError
import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.port.repository.ServiceOrderApprovalRepository
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.port.security.SecurityHasher
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SendEmailApprovalUseCaseImpl(
    private val emailSender: EmailSender,
    private val emailQueueRepository: EmailQueueRepository,
    private val serviceOrderApprovalRepository: ServiceOrderApprovalRepository,
    private val securityHasher: SecurityHasher,
    private val approvalLinkGenerator: ApprovalLinkGenerator,
    private val updateServiceOrderUseCase: UpdateServiceOrderUseCase,
    @Named("applicationScope") private val scope: CoroutineScope,
) : BaseUseCaseImpl<Int, Unit>(),
    SendEmailApprovalUseCase {
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
                scope.launch {
                    updateServiceOrderUseCase.execute(
                        UpdateServiceOrderCommand(
                            code = emailItem.orderCode,
                            operatorRole = Role.MANAGER,
                            status = ServiceOrderStatus.WAITING_APPROVAL,
                            complaint = null,
                        ),
                    )
                }
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
