package com.khrix.application.email

import com.khrix.application.email.model.generateHtmlApprovalRequestTemplate
import com.khrix.application.email.model.generateHtmlStatusUpdateTemplate
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.valueobject.user.Email

data class EmailMessageBody(
    val toRecipients: List<Email>,
    val subject: String,
    val body: String,
)

fun EmailQueueItem.toStatusUpdateEmail(): EmailMessageBody =
    EmailMessageBody(
        toRecipients = listOf(Email(recipient)),
        subject = subject,
        body = (metadata).generateHtmlStatusUpdateTemplate(),
    )

fun EmailQueueItem.toApprovalEmail(
    approvalWebhookUrl: String,
    newOrder: Boolean,
): EmailMessageBody =
    EmailMessageBody(
        toRecipients = listOf(Email(recipient)),
        subject = subject,
        body =
            (metadata)
                .generateHtmlApprovalRequestTemplate(approvalWebhookUrl, newOrder),
    )

interface EmailSender {
    suspend fun send(message: EmailMessageBody)
}
