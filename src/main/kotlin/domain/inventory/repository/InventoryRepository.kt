package com.khrix.domain.inventory.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.inventory.model.InventoryItem

interface InventoryRepository :
    BaseUpdate<InventoryItem>,
    BaseCreate<InventoryItem>,
    BaseCreateReturn<InventoryItem>,
    BaseDelete {
    suspend fun decrementItemQuantity(inventoryId: Int, quantityDecrement: Int)
    suspend fun incrementItemQuantity(inventoryId: Int, quantityIncrement: Int)
    suspend fun getByIdOrSku(inventoryId: String): InventoryItem?
    suspend fun getByIdOrSku(inventoryId: List<String>): List<InventoryItem>
}


