package com.khrix.adapter.exposed.serviceorder.mapper

import com.khrix.adapter.exposed.inventory.mapper.toModel
import com.khrix.adapter.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.adapter.exposed.user.mapper.toModel
import com.khrix.adapter.exposed.vehicles.mapper.toModel
import com.khrix.domain.serviceorder.model.ServiceOrder
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun ServiceOrderEntity.toModel(): ServiceOrder =
    suspendTransaction {
        ServiceOrder(
            client = client.toModel(),
            operator = operator.toModel(),
            vehicle = vehicle.toModel(),
            status = status,
            complaint = complaint,
            diagnosis = diagnosis,
            tasks = tasks.map { it.toModel(this@toModel.id.value) },
            inventoryItems = parts.map { it.toModel() },
            id = this@toModel.id.value,
        ).apply {
            code = this@toModel.code
        }
    }
