package com.khrix.adapter.outbound.exposed.email.repository

import com.khrix.adapter.outbound.exposed.BaseExposedRepository
import com.khrix.adapter.outbound.exposed.email.database.EmailQueueEntity
import com.khrix.adapter.outbound.exposed.email.database.EmailQueueTable
import com.khrix.adapter.outbound.exposed.email.mapper.toModel
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.repository.EmailQueueRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.update

class EmailQueueExposedQueueRepositoryImpl(
    database: Database,
) : BaseExposedRepository<EmailQueueEntity, EmailQueueItem>(database),
    EmailQueueRepository {
    override suspend fun create(data: EmailQueueItem): Int =
        suspendedQuery {
            EmailQueueEntity
                .new {
                    recipient = data.recipient
                    subject = data.subject
                    metadata = data.metadata
                    status = data.status
                    attempts = data.attempts
                    errorMessage = data.errorMessage
                    code = data.orderCode
                }.id.value
        }

    override suspend fun read(id: Int): EmailQueueItem? =
        suspendedQuery {
            EmailQueueEntity.findById(id)?.toModel()
        }

    override suspend fun registerAttempt(
        id: Int,
        status: EmailStatus,
    ): EmailQueueItem =
        suspendedQuery {
            EmailQueueTable.update({ EmailQueueTable.id eq id }) {
                it[EmailQueueTable.attempts] = EmailQueueTable.attempts + 1
                it[EmailQueueTable.status] = status
            }
            EmailQueueEntity.findById(id)!!.toModel()
        }

    override suspend fun setErrorMessage(
        id: Int,
        errorMessage: String,
    ) {
        suspendedQuery {
            EmailQueueEntity.findByIdAndUpdate(id) {
                it.errorMessage = errorMessage
                it.status = EmailStatus.FAILED
            }
        }
    }
}
