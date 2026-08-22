package com.khrix.adapter.azure

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.EmailClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.khrix.adapter.app.InfraConfig

class AzureCredentialConnection(
    infraConfig: InfraConfig,
) {
    private val azureConfig = infraConfig.azureConfig

    private val azureKeyCredential = AzureKeyCredential(azureConfig.accessKey)

    fun createAzureConnection(): EmailAsyncClient =
        try {
            EmailClientBuilder()
                .endpoint(azureConfig.communicationEndpoint)
                .credential(azureKeyCredential)
                .buildAsyncClient()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create Azure Email client: ${e.message}", e)
        }
}
