package com.khrix.adapter.azure

import com.khrix.adapter.azure.email.AzureEmailSender
import com.khrix.application.email.EmailSender
import io.ktor.server.plugins.di.DependencyRegistry

fun azureDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(AzureCredentialConnection::class)
        provide<EmailSender>(AzureEmailSender::class)
    }
}
