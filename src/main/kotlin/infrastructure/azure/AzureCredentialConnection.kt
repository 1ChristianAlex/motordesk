package com.khrix.infrastructure.azure

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.EmailClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.khrix.infrastructure.app.loadProperties
import java.util.Properties

object AzureCredentialConnection {
    private val properties: Properties by lazy {
        loadProperties()
    }

    private val azureKeyCredential = AzureKeyCredential(properties.getProperty("azure.communication.access-key"))

    fun createAzureConnection(): EmailAsyncClient {
        val endpoint = properties.getProperty("azure.communication.endpoint")
        return EmailClientBuilder()
            .endpoint(endpoint)
            .credential(azureKeyCredential)
            .buildAsyncClient()
    }
}
