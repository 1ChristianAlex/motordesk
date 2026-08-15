package com.khrix.infrastructure.azure

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.EmailClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.khrix.infrastructure.app.InfraCredentials

class AzureCredentialConnection(
    infraCredentials: InfraCredentials,
) {
    private val azureConfig = infraCredentials.azureConfig

    private val azureKeyCredential = AzureKeyCredential(azureConfig.accessKey)

    fun createAzureConnection(): EmailAsyncClient =
        try {
            EmailClientBuilder()
                .endpoint(azureConfig.accessKey)
                .credential(azureKeyCredential)
                .buildAsyncClient()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create Azure Email client: ${e.message}", e)
        }
}
