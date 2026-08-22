package com.khrix.adapter.outbound.exposed.serviceorder.mapper

import com.khrix.adapter.outbound.exposed.serviceorder.database.OrderApprovalEntity
import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun OrderApprovalEntity.toModel(): ServiceOrderApprovalToken =
    suspendTransaction {
        ServiceOrderApprovalToken(
            serviceOrderCode = serviceOrder.code,
            tokenHash = tokenHash,
            expiresAt = expiresAt,
            _usedAt = usedAt,
            id = this@toModel.id.value,
        )
    }
