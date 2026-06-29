package com.khrix.infrastructure.exposed.email.repository

import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.email.database.EmailQueueEntity
import com.khrix.infrastructure.exposed.email.database.EmailQueueTable
import com.khrix.infrastructure.exposed.email.mapper.toModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.update

class EmailQueueExposedQueueRepositoryImpl(
    database: Database,
) : BaseExposedRepository<EmailQueueEntity, EmailQueueItem>(database),
    EmailQueueRepository {
    override suspend fun createRead(data: EmailQueueItem): EmailQueueItem =
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
                }.toModel()
        }

    override suspend fun read(id: Int): EmailQueueItem? =
        suspendedQuery {
            EmailQueueEntity.findById(id)?.toModel()
        }

    override suspend fun incrementAttempt(id: Int) {
        suspendedQuery {
            EmailQueueTable.update({ EmailQueueTable.id eq id }) {
                it[EmailQueueTable.attempts] = EmailQueueTable.attempts + 1
            }
        }
    }

    override suspend fun setErrorMessage(
        id: Int,
        errorMessage: String,
    ) {
        suspendedQuery {
            EmailQueueEntity.findByIdAndUpdate(id) {
                it.errorMessage = errorMessage
            }
        }
    }

    override suspend fun changeStatus(
        id: Int,
        status: EmailStatus,
    ) {
        suspendedQuery {
            EmailQueueEntity.findByIdAndUpdate(id) {
                it.status = status
            }
        }
    }
}
