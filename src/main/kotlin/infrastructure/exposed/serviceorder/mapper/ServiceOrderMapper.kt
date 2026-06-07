package com.khrix.infrastructure.exposed.serviceorder.mapper

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.infrastructure.exposed.inventory.mapper.toModel
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.infrastructure.exposed.user.mapper.toModel
import com.khrix.infrastructure.exposed.vehicles.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun ServiceOrderEntity.toModel(): ServiceOrder {
    return suspendTransaction {
        ServiceOrder(
            client = client.toModel(),
            operator = operator.toModel(),
            vehicle = vehicle.toModel(),
            status = status,
            complaint = complaint,
            diagnosis = diagnosis,
            totalAmount = totalAmount,
            tasks = tasks.map { it.toModel() },
            inventoryItems = parts.map { it.toModel() },
        )
    }
}
