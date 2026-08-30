package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.inventory.port.usecase.CreateInventoryUseCase

class CreateInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<InventoryItem, InventoryItem>(),
    CreateInventoryUseCase {
    override suspend fun internalExecute(command: InventoryItem): InventoryItem = inventoryRepository.createRead(command)

    override suspend fun useCaseDescription(): String = "Create a new inventory item"
}
