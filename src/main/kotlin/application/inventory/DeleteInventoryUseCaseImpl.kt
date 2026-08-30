package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.inventory.port.usecase.DeleteInventoryUseCase

class DeleteInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<Int, Unit>(),
    DeleteInventoryUseCase {
    override suspend fun internalExecute(command: Int) {
        inventoryRepository.delete(command)
    }

    override suspend fun useCaseDescription(): String = "Delete an existing inventory item"
}
