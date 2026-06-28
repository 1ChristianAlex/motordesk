package com.khrix.domain.email.repository

import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseRead
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus

interface EmailQueueRepository :
    BaseCreateReturn<EmailQueueItem>,
    BaseRead<EmailQueueItem> {
    suspend fun incrementAttempt(id: Int)
    suspend fun setErrorMessage(id: Int, errorMessage: String)
    suspend fun changeStatus(id: Int, status: EmailStatus)
}



