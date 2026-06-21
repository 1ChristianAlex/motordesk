package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class DeleteInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = DeleteInventoryUseCaseImpl(inventoryRepository)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assert(impl.useCaseDescription().contains("Delete an existing inventory item"))
        }
    }

    @Test
    fun `internalExecute calls delete on repository`() {
        runBlocking {
            val id = 1
            coEvery { inventoryRepository.delete(id) } returns Unit

            impl.execute(id).getOrThrow()
            coVerify { inventoryRepository.delete(id) }
        }
    }
}


