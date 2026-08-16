package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.inventory.usecase.GetInventoryByIdOrSkuUseCase

class GetInventoryByIdOrSkuUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<String, InventoryItem>(),
    GetInventoryByIdOrSkuUseCase {
    override suspend fun internalExecute(command: String): InventoryItem =
        inventoryRepository.getByIdOrSku(command)
            ?: throw NoSuchElementException("Inventory item with id or sku '$command' not found")

    override suspend fun useCaseDescription(): String = "Update an existing inventory item"
}
