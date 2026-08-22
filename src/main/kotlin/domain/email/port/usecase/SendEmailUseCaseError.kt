package com.khrix.domain.email.port.usecase

sealed class SendEmailUseCaseError(
    override val message: String?,
) : Exception(message) {
    class NotFound : SendEmailUseCaseError("Resource not available to create email template")

    class Retry : SendEmailUseCaseError("Email sending failed, retrying...")

    class NoMoreRetriesAvailable : SendEmailUseCaseError("Email sending failed, no more retries available")
}
