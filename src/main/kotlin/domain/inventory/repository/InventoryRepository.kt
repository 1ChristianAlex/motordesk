package com.khrix.domain.inventory.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.inventory.model.InventoryItem

interface InventoryRepository :
    BaseRead<InventoryItem>,
    BaseUpdate<InventoryItem>,
    BaseCreate<InventoryItem>,
    BaseDelete


