package com.khrix.infrastructure.exposed.inventory.repository

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.inventory.database.InventoryTable
import com.khrix.infrastructure.exposed.inventory.mapper.toModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.minus
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.update

class InventoryExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<InventoryEntity, InventoryItem>(database),
    InventoryRepository {
    override suspend fun update(
        id: Int,
        data: InventoryItem,
    ) {
        suspendedQuery {
            val inventory = InventoryEntity[id]

            if (data.quantity > inventory.quantity) {
                this.incrementItemQuantity(id, data.quantity - inventory.quantity)
            }
            if (data.quantity < inventory.quantity) {
                this.incrementItemQuantity(id, data.quantity + inventory.quantity)
            }

            InventoryEntity.findByIdAndUpdate(id) {
                it.sku = data.sku
                it.name = data.name
                it.description = data.description
                it.minimumQuantity = data.minimumQuantity
                it.unitPrice = data.unitPrice.value
                it.isActive = data.isActive
            }
        }
    }

    override suspend fun create(data: InventoryItem): Int = createTask(data).id.value

    private suspend fun createTask(data: InventoryItem) =
        suspendedQuery {
            InventoryEntity.new {
                sku = data.sku
                name = data.name
                description = data.description
                quantity = data.quantity
                minimumQuantity = data.minimumQuantity
                unitPrice = data.unitPrice.value
                isActive = data.isActive
            }
        }

    override suspend fun delete(id: Int) {
        suspendedQuery {
            InventoryEntity.findByIdAndUpdate(id) {
                it.isActive = false
            }
        }
    }

    override suspend fun decrementItemQuantity(
        inventoryId: Int,
        quantityDecrement: Int,
    ) {
        suspendedQuery {
            InventoryTable.update({ InventoryTable.id eq inventoryId }) {
                it[InventoryTable.quantity] = InventoryTable.quantity - quantityDecrement
            }
        }
    }

    override suspend fun incrementItemQuantity(
        inventoryId: Int,
        quantityIncrement: Int,
    ) {
        suspendedQuery {
            InventoryTable.update({ InventoryTable.id eq inventoryId }) {
                it[InventoryTable.quantity] = InventoryTable.quantity + quantityIncrement
            }
        }
    }

    override suspend fun getByIdOrSku(inventoryId: String): InventoryItem? =
        suspendedQuery {
            InventoryEntity
                .find { InventoryTable.id eq inventoryId.toInt() or (InventoryTable.sku eq inventoryId) }
                .firstOrNull()
                ?.toModel()
        }

    override suspend fun getByIdOrSku(inventoryId: List<String>): List<InventoryItem> =
        suspendedQuery {
            InventoryEntity
                .find {
                    InventoryTable.id inList inventoryId.mapNotNull { it.toIntOrNull() } or
                        (InventoryTable.sku inList inventoryId)
                }.map { it.toModel() }
        }

    override suspend fun createRead(data: InventoryItem): InventoryItem = createTask(data).toModel()
}
