package com.khrix.infrastructure.exposed.email.mapper

import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.infrastructure.exposed.email.database.EmailQueueEntity

fun EmailQueueEntity.toModel(): EmailQueueItem = EmailQueueItem(
    id = id.value,
    recipient = recipient,
    subject = subject,
    metadata = metadata,
    status = status,
    attempts = attempts,
    errorMessage = errorMessage,
    orderCode = code
)

