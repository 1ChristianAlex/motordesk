package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.domain.inventory.usecase.DeleteInventoryUseCase

class DeleteInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : DeleteInventoryUseCase, BaseUseCaseImpl<Int, Unit>() {
    override suspend fun internalExecute(command: Int): Unit {
        inventoryRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Delete an existing inventory item"
    }
}
