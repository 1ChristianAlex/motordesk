package com.khrix.adapter.outbound.email.aws

import com.khrix.adapter.app.AwsEmailConfig
import com.khrix.application.email.EmailMessageBody
import com.khrix.application.email.EmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sesv2.SesV2Client
import software.amazon.awssdk.services.sesv2.model.Body
import software.amazon.awssdk.services.sesv2.model.Content
import software.amazon.awssdk.services.sesv2.model.Destination
import software.amazon.awssdk.services.sesv2.model.EmailContent
import software.amazon.awssdk.services.sesv2.model.Message
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest

class AwsSesEmailSender(
    private val config: AwsEmailConfig,
    private val sesClient: SesV2Client =
        SesV2Client
            .builder()
            .region(Region.of(config.region))
            .build(),
) : EmailSender,
    AutoCloseable {
    override suspend fun send(message: EmailMessageBody) {
        val request =
            SendEmailRequest
                .builder()
                .fromEmailAddress(config.fromAddress)
                .destination(
                    Destination
                        .builder()
                        .toAddresses(message.toRecipients.map { it.value })
                        .build(),
                ).content(
                    EmailContent
                        .builder()
                        .simple(
                            Message
                                .builder()
                                .subject(Content.builder().data(message.subject).build())
                                .body(
                                    Body
                                        .builder()
                                        .html(Content.builder().data(message.body).build())
                                        .build(),
                                ).build(),
                        ).build(),
                ).build()

        withContext(Dispatchers.IO) {
            sesClient.sendEmail(request)
        }
    }

    override fun close() {
        sesClient.close()
    }
}
