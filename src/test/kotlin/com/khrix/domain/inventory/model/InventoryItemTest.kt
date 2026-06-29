package com.khrix.domain.inventory.model

import com.khrix.testutils.sampleInventoryItem
import kotlin.test.Test
import kotlin.test.assertEquals

class InventoryItemTest {
    @Test
    fun `retains stock and pricing data`() {
        val item = sampleInventoryItem(3)
        assertEquals("SKU3", item.sku)
        assertEquals(10, item.quantity)
    }
}
