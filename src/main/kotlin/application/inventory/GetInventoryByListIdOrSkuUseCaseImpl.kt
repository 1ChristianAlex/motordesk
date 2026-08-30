package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase

class GetInventoryByListIdOrSkuUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : GetInventoryByListIdOrSkuUseCase, BaseUseCaseImpl<List<String>, List<InventoryItem>>() {
    override suspend fun internalExecute(command: List<String>): List<InventoryItem> {
        return inventoryRepository.getByIdOrSku(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Update an existing inventory item"
    }
}
