package com.khrix.infrastructure.exposed.email.database

import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import com.khrix.infrastructure.exposed.BaseTable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.json.jsonb

object EmailQueueTable : BaseTable("emailQueue") {

    val recipient = varchar("recipient", 255)

    val subject = varchar("subject", 255)

    val metadata = jsonb<ServiceOrderEmailMetadata>("metadata", Json.Default)

    val status = enumeration(
        "status", EmailStatus::class
    ).default(EmailStatus.PENDING)

    val attempts = integer("attempts").default(0)

    val errorMessage = text("errorMessage").nullable()
}

class EmailQueueEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EmailQueueEntity>(EmailQueueTable)

    var recipient by EmailQueueTable.recipient
    var subject by EmailQueueTable.subject
    var metadata by EmailQueueTable.metadata
    var status by EmailQueueTable.status
    var attempts by EmailQueueTable.attempts
    var errorMessage by EmailQueueTable.errorMessage
}