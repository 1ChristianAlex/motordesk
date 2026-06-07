package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.inventory.usecase.CreateInventoryUseCase

class CreateInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : CreateInventoryUseCase, BaseUseCaseImpl<InventoryItem, InventoryItem>() {
    override suspend fun internalExecute(command: InventoryItem): InventoryItem {
        return inventoryRepository.createRead(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Create a new inventory item"
    }
}
