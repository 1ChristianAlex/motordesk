package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.inventory.usecase.UpdateInventoryUseCase

class UpdateInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : UpdateInventoryUseCase, BaseUseCaseImpl<InventoryItem, Unit>() {
    override suspend fun internalExecute(command: InventoryItem): Unit {
        return inventoryRepository.update(command.id, command)
    }

    override suspend fun useCaseDescription(): String {
        return "Update an existing inventory item"
    }
}
