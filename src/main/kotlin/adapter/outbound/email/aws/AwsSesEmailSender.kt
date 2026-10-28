package com.khrix.adapter.outbound.email.aws

import aws.sdk.kotlin.services.sesv2.SesV2Client
import aws.sdk.kotlin.services.sesv2.model.Body
import aws.sdk.kotlin.services.sesv2.model.Content
import aws.sdk.kotlin.services.sesv2.model.Destination
import aws.sdk.kotlin.services.sesv2.model.EmailContent
import aws.sdk.kotlin.services.sesv2.model.Message
import aws.sdk.kotlin.services.sesv2.model.SendEmailRequest
import com.khrix.adapter.app.AwsEmailConfig
import com.khrix.application.email.EmailMessageBody
import com.khrix.application.email.EmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AwsSesEmailSender(
    private val config: AwsEmailConfig,
    private val sesClientFactory: suspend () -> SesV2Client = {
        SesV2Client.fromEnvironment { region = config.region }
    },
) : EmailSender {
    override suspend fun send(message: EmailMessageBody) {
        val destinationOb =
            Destination {
                toAddresses = message.toRecipients.map { it.value }
            }

        val contentOb =
            Content {
                data = message.body
            }

        val subOb =
            Content {
                data = message.subject
            }

        val bodyOb =
            Body {
                html = contentOb
            }

        val msgOb =
            Message {
                subject = subOb
                body = bodyOb
            }

        val emailRequest =
            SendEmailRequest {
                destination = destinationOb
                content =
                    EmailContent {
                        simple = msgOb
                    }
                fromEmailAddress = config.fromAddress
            }

        withContext(Dispatchers.IO) {
            sesClientFactory().use { sesClient -> sesClient.sendEmail(emailRequest) }
        }
    }
}
