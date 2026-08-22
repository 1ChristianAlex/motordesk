package com.khrix.adapter.outbound.exposed.serviceorder.database

import com.khrix.adapter.outbound.exposed.BaseTable
import com.khrix.adapter.outbound.exposed.DatabaseSchemas
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp

object OrderApprovalTable : BaseTable("orderApprovals", DatabaseSchemas.SERVICE_ORDER) {
    val serviceOrder = reference("serviceOrderId", ServiceOrdersTable)
    val tokenHash = text("tokenHash")

    val expiresAt = timestamp("expiresAt")
    val usedAt = timestamp("usedAt").nullable()
}

class OrderApprovalEntity(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<OrderApprovalEntity>(
        OrderApprovalTable,
    )

    var serviceOrder by ServiceOrderEntity referencedOn OrderApprovalTable.serviceOrder

    var tokenHash by OrderApprovalTable.tokenHash

    var expiresAt by OrderApprovalTable.expiresAt
    var usedAt by OrderApprovalTable.usedAt

    var createdAt by OrderApprovalTable.createdAt
    var updatedAt by OrderApprovalTable.updatedAt
}
