package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleInventoryItem
import kotlin.test.Test
import kotlin.test.assertEquals

class GetInventoryByListIdOrSkuUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = GetInventoryByListIdOrSkuUseCaseImpl(inventoryRepository)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assert(impl.useCaseDescription().contains("Update an existing inventory item"))
        }
    }

    @Test
    fun `internalExecute returns items from repository`() {
        runBlocking {
            val items = listOf(sampleInventoryItem())
            coEvery { inventoryRepository.getByIdOrSku(listOf("1")) } returns items

            val res = impl.execute(listOf("1"))
            assertEquals(items, res.getOrThrow())
        }
    }
}


