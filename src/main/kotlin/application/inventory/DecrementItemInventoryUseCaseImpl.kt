package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.inventory.port.usecase.DecrementItemInventoryCommand
import com.khrix.domain.inventory.port.usecase.DecrementItemInventoryUseCase

class DecrementItemInventoryUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<DecrementItemInventoryCommand, Unit>(),
    DecrementItemInventoryUseCase {
    override suspend fun internalExecute(command: DecrementItemInventoryCommand) {
        inventoryRepository.decrementItemQuantity(command.itemId, command.quantity)

        val afterDecrement =
            inventoryRepository.getByIdOrSku(command.itemId.toString())
                ?: throw NoSuchElementException("No item with id $command.itemId")

        if (afterDecrement.quantity <= 0) {
            inventoryRepository.delete(command.itemId)
        }
    }

    override suspend fun useCaseDescription(): String =
        "Safely decrement the quantity of an inventory item, ensuring it does not go below zero"
}
