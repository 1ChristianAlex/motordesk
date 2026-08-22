package com.khrix.adapter.outbound.exposed.email.mapper

import com.khrix.adapter.outbound.exposed.email.database.EmailQueueEntity
import com.khrix.domain.email.model.EmailQueueItem

fun EmailQueueEntity.toModel(): EmailQueueItem =
    EmailQueueItem(
        id = id.value,
        recipient = recipient,
        subject = subject,
        metadata = metadata,
        status = status,
        attempts = attempts,
        errorMessage = errorMessage,
        orderCode = code,
    )
