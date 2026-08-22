package com.khrix.application.inventory

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.inventory.port.repository.InventoryRepository
import com.khrix.domain.inventory.port.usecase.GetInventoryByListIdOrSkuUseCase

class GetInventoryByListIdOrSkuUseCaseImpl(
    private val inventoryRepository: InventoryRepository,
) : BaseUseCaseImpl<List<String>, List<InventoryItem>>(),
    GetInventoryByListIdOrSkuUseCase {
    override suspend fun internalExecute(command: List<String>): List<InventoryItem> = inventoryRepository.getByIdOrSku(command)

    override suspend fun useCaseDescription(): String = "Update an existing inventory item"
}
