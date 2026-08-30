package com.khrix.domain.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailQueueItem(
    val id: Int,
    val orderCode: String,
    val recipient: String,
    val subject: String,
    val metadata: ServiceOrderEmailMetadata,
    val status: EmailStatus,
    val attempts: Int,
    val errorMessage: String?,
)
