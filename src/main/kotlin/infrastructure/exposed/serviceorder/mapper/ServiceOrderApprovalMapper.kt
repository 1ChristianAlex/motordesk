package com.khrix.infrastructure.exposed.serviceorder.mapper

import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken
import com.khrix.infrastructure.exposed.serviceorder.database.OrderApprovalEntity
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun OrderApprovalEntity.toModel(): ServiceOrderApprovalToken =
    suspendTransaction {
        ServiceOrderApprovalToken(
            serviceOrderCode = serviceOrder.code,
            tokenHash = tokenHash,
            expiresAt = expiresAt,
            usedAt = usedAt,
            revokedAt = revokedAt,
            id = this@toModel.id.value,
        )
    }
