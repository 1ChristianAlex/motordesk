package com.khrix.adapter.outbound.email.aws

import com.khrix.adapter.app.AwsEmailConfig
import com.khrix.application.email.EmailMessageBody
import com.khrix.domain.valueobject.user.Email
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.services.sesv2.SesV2Client
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class AwsSesEmailSenderTest {
    @Test
    fun `sends an html email using the configured sender and recipients`() =
        runBlocking {
            val sesClient = mockk<SesV2Client>()
            val request = slot<SendEmailRequest>()
            every { sesClient.sendEmail(capture(request)) } returns
                SendEmailResponse.builder().messageId("message-id").build()

            val sender =
                AwsSesEmailSender(
                    config = AwsEmailConfig(region = "us-east-1", fromAddress = "service@example.com"),
                    sesClient = sesClient,
                )

            sender.send(
                EmailMessageBody(
                    toRecipients = listOf(Email("customer@example.com")),
                    subject = "Service order update",
                    body = "<p>Your vehicle is ready.</p>",
                ),
            )

            assertEquals("service@example.com", request.captured.fromEmailAddress())
            assertEquals(listOf("customer@example.com"), request.captured.destination().toAddresses())
            assertEquals(
                "Service order update",
                request.captured
                    .content()
                    .simple()
                    .subject()
                    .data(),
            )
            assertEquals(
                "<p>Your vehicle is ready.</p>",
                request.captured
                    .content()
                    .simple()
                    .body()
                    .html()
                    .data(),
            )
            verify(exactly = 1) { sesClient.sendEmail(any<SendEmailRequest>()) }
        }
}
