package com.khrix.domain.email.model

data class EmailQueueItem(
    val id: Int,
    val recipient: String,
    val subject: String,
    val metadata: ServiceOrderEmailMetadata,
    val status: EmailStatus,
    val attempts: Int,
    val errorMessage: String?
)