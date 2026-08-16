package com.khrix.infrastructure.azure.email

import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.azure.communication.email.models.EmailSendResult
import com.azure.core.util.polling.PollerFlux
import com.khrix.application.email.EmailMessageBody
import com.khrix.application.email.EmailSender
import com.khrix.infrastructure.azure.AzureCredentialConnection
import kotlinx.coroutines.reactor.awaitSingle

class AzureEmailSender(
    credentialConnection: AzureCredentialConnection,
) : EmailSender {
    private val emailAsyncClient = credentialConnection.createAzureConnection()

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

        emailAsyncClient.beginSend(emailMessage).awaitResult()
    }

    private suspend fun PollerFlux<EmailSendResult, EmailSendResult>.awaitResult(): EmailSendResult =
        last().flatMap { it.getFinalResult() }.awaitSingle()
}
