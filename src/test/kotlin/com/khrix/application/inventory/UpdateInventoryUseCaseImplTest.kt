package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import testutils.sampleInventoryItem
import kotlin.test.Test

class UpdateInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = UpdateInventoryUseCaseImpl(inventoryRepository)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assert(impl.useCaseDescription().contains("Update an existing inventory item"))
        }
    }

    @Test
    fun `internalExecute calls update on repository`() {
        runBlocking {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.update(item.id, item) } returns Unit

            impl.execute(item).getOrThrow()
            coVerify { inventoryRepository.update(item.id, item) }
        }
    }
}


