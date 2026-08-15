package com.khrix.infrastructure.azure

import com.khrix.application.notification.EmailSender
import com.khrix.infrastructure.azure.email.AzureEmailSender
import io.ktor.server.plugins.di.DependencyRegistry

fun azureDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(AzureCredentialConnection::class)
        provide<EmailSender>(AzureEmailSender::class)
    }
}
