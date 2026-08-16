package com.khrix.domain.email.usecase

import com.khrix.domain.core.BaseUseCase

sealed class SendEmailUseCaseError(
    override val message: String?,
) : Exception(message) {
    class NotFound : SendEmailUseCaseError("Resource not available to create email template")

    class Retry : SendEmailUseCaseError("Email sending failed, retrying...")

    class NoMoreRetriesAvailable : SendEmailUseCaseError("Email sending failed, no more retries available")
}

interface SendEmailUseCase : BaseUseCase<Int, Unit>
