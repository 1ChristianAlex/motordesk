package com.khrix.adapter.outbound.email

import com.khrix.adapter.app.InfraConfig
import com.khrix.adapter.outbound.email.aws.AwsSesEmailSender
import com.khrix.application.email.EmailSender
import io.ktor.server.plugins.di.DependencyRegistry

fun emailDI(
    dependencies: DependencyRegistry,
    infraConfig: InfraConfig,
): EmailSender {
    val emailSender = AwsSesEmailSender(infraConfig.awsEmailConfig)

    dependencies.provide<EmailSender> { emailSender }
    return emailSender
}
