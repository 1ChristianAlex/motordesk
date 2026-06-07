package com.khrix.infrastructure.exposed.inventory.repository

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.inventory.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database

class InventoryExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<InventoryEntity, InventoryItem>(database), InventoryRepository {
    override suspend fun read(id: Int): InventoryItem? {
        return suspendedQuery {
            InventoryEntity.findById(id)?.toModel()
        }
    }

    override suspend fun update(id: Int, data: InventoryItem) {
        suspendedQuery {
            InventoryEntity.findByIdAndUpdate(id) {
                it.sku = data.sku
                it.name = data.name
                it.description = data.description
                it.quantity = data.quantity
                it.minimumQuantity = data.minimumQuantity
                it.unitPrice = data.unitPrice
                it.isActive = data.isActive
            }
        }
    }

    override suspend fun create(data: InventoryItem): Int {
        return createTask(data).id.value
    }

    private suspend fun createTask(data: InventoryItem) = suspendedQuery {
        InventoryEntity.new {
            sku = data.sku
            name = data.name
            description = data.description
            quantity = data.quantity
            minimumQuantity = data.minimumQuantity
            unitPrice = data.unitPrice
            isActive = data.isActive
        }
    }

    override suspend fun delete(id: Int) {
        this.update(
            id,
            read(id)?.copy(isActive = false) ?: throw IllegalArgumentException("InventoryItem with id $id not found")
        )
    }
}
