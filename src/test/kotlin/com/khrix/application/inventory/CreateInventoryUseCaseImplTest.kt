package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleInventoryItem
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = CreateInventoryUseCaseImpl(inventoryRepository)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assertEquals("Create a new inventory item", impl.useCaseDescription())
        }
    }

    @Test
    fun `internalExecute calls repository createRead`() {
        runBlocking {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.createRead(item) } returns item

            val res = impl.execute(item)
            assertEquals(item, res.getOrThrow())
            coVerify { inventoryRepository.createRead(item) }
        }
    }
}

