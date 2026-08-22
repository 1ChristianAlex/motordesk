package com.khrix.adapter.azure.email

import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.azure.communication.email.models.EmailSendResult
import com.azure.core.util.polling.PollerFlux
import com.khrix.adapter.azure.AzureCredentialConnection
import com.khrix.application.email.EmailMessageBody
import com.khrix.application.email.EmailSender
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory

class AzureEmailSender(
    credentialConnection: AzureCredentialConnection,
) : EmailSender {
    private val emailAsyncClient = credentialConnection.createAzureConnection()
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun send(message: EmailMessageBody) {
        val emailMessage =
            EmailMessage().apply {
                message.run {
                    setSenderAddress(senderAddress.value)
                    setToRecipients(
                        toRecipients.map {
                            logger.info("Sending email to " + it.value)
                            EmailAddress(it.value)
                        },
                    )
                    setSubject(subject)
                    setBodyHtml(body)
                }
            }

        emailAsyncClient.beginSend(emailMessage).awaitResult()
    }

    private suspend fun PollerFlux<EmailSendResult, EmailSendResult>.awaitResult(): EmailSendResult =
        last().flatMap { it.getFinalResult() }.awaitSingle()
}
