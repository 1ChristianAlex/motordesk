package com.khrix.infrastructure.azure.email

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.khrix.application.notification.EmailMessageBody
import com.khrix.application.notification.EmailSender

class AzureEmailSender constructor(
    private val emailAsyncClient: EmailAsyncClient,
) : EmailSender {
    override suspend fun send(message: EmailMessageBody) {
        val emailMessage =
            EmailMessage().apply {
                message.run {
                    setSenderAddress("<${senderAddress.value}>")
                    setToRecipients(toRecipients.map { EmailAddress(it.value) })
                    setSubject(subject)
                    setBodyHtml(bodyPlainText)
                }
            }
    }
}
