package com.khrix.adapter.outbound.email.aws

import aws.sdk.kotlin.services.sesv2.SesV2Client
import aws.sdk.kotlin.services.sesv2.model.SendEmailRequest
import aws.sdk.kotlin.services.sesv2.model.SendEmailResponse
import com.khrix.adapter.app.AwsEmailConfig
import com.khrix.application.email.EmailMessageBody
import com.khrix.domain.valueobject.user.Email
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class AwsSesEmailSenderTest {
    @Test
    fun `sends an html email using the configured sender and recipients`() =
        runBlocking {
            val sesClient = mockk<SesV2Client>()
            val request = slot<SendEmailRequest>()
            coEvery { sesClient.sendEmail(capture(request)) } returns SendEmailResponse {}
            every { sesClient.close() } just Runs

            val sender =
                AwsSesEmailSender(
                    config = AwsEmailConfig(region = "us-east-1", fromAddress = "service@example.com"),
                    sesClientFactory = { sesClient },
                )

            sender.send(
                EmailMessageBody(
                    toRecipients = listOf(Email("customer@example.com")),
                    subject = "Service order update",
                    body = "<p>Your vehicle is ready.</p>",
                ),
            )

            assertEquals("service@example.com", request.captured.fromEmailAddress)
            assertEquals(listOf("customer@example.com"), request.captured.destination?.toAddresses)
            assertEquals(
                "Service order update",
                request.captured
                    .content
                    ?.simple
                    ?.subject
                    ?.data,
            )
            assertEquals(
                "<p>Your vehicle is ready.</p>",
                request.captured
                    .content
                    ?.simple
                    ?.body
                    ?.html
                    ?.data,
            )
            coVerify(exactly = 1) { sesClient.sendEmail(any<SendEmailRequest>()) }
        }
}
