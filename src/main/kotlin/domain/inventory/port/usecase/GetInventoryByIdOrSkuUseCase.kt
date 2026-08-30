package com.khrix.domain.inventory.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.inventory.model.InventoryItem

interface GetInventoryByIdOrSkuUseCase : BaseUseCase<String, InventoryItem>
