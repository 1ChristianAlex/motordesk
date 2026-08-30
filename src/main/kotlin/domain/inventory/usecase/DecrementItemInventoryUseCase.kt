package com.khrix.domain.inventory.usecase

import com.khrix.domain.core.BaseUseCase

data class DecrementItemInventoryCommand(
    val itemId: Int,
    val quantity: Int,
)

interface DecrementItemInventoryUseCase : BaseUseCase<DecrementItemInventoryCommand, Unit>

