package com.khrix.domain.email.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseRead
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus

interface EmailQueueRepository :
    BaseCreate<EmailQueueItem>,
    BaseRead<EmailQueueItem> {
    suspend fun setErrorMessage(
        id: Int,
        errorMessage: String,
    )

    suspend fun registerAttempt(
        id: Int,
        status: EmailStatus,
    ): EmailQueueItem
}
