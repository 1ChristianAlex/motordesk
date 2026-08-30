package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.inventory.port.usecase.UpdateInventoryUseCase

class UpdateInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<InventoryItem, Unit>(),
    UpdateInventoryUseCase {
    override suspend fun internalExecute(command: InventoryItem) = inventoryRepository.update(command.id, command)

    override suspend fun useCaseDescription(): String = "Update an existing inventory item"
}
