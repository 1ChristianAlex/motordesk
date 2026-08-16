package com.khrix.application.email

import com.khrix.application.email.model.EmailTemplating
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.valueobject.user.Email

data class EmailMessageBody(
    val toRecipients: List<Email>,
    val subject: String,
    val body: String,
) {
    val senderAddress: Email = Email("donotreply@motordesk.azurecomm.net")
}

fun EmailQueueItem.toStatusUpdateEmail(): EmailMessageBody =
    EmailMessageBody(
        toRecipients = listOf(Email(recipient)),
        subject = subject,
        body = EmailTemplating.FromServiceOrderEmailMetadata(metadata).generateHtmlStatusUpdateTemplate(),
    )

fun EmailQueueItem.toApprovalEmail(
    approvalWebhookUrl: String,
    newOrder: Boolean,
): EmailMessageBody =
    EmailMessageBody(
        toRecipients = listOf(Email(recipient)),
        subject = subject,
        body =
            EmailTemplating
                .FromServiceOrderEmailMetadata(metadata)
                .generateHtmlApprovalRequestTemplate(approvalWebhookUrl, newOrder),
    )

interface EmailSender {
    suspend fun send(message: EmailMessageBody)
}
