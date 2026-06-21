package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith
import testutils.sampleInventoryItem

class DecrementItemInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = DecrementItemInventoryUseCaseImpl(inventoryRepository)

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assert(impl.useCaseDescription().contains("Safely decrement"))
        }
    }

    @Test
    fun `internalExecute deletes item when quantity becomes zero or less`() {
        runBlocking {
            val cmd = com.khrix.domain.inventory.usecase.DecrementItemInventoryCommand(itemId = 1, quantity = 5)

            coEvery { inventoryRepository.decrementItemQuantity(cmd.itemId, cmd.quantity) } returns Unit
            coEvery { inventoryRepository.getByIdOrSku(cmd.itemId.toString()) } returns sampleInventoryItem().copy(quantity = 0)
            coEvery { inventoryRepository.delete(cmd.itemId) } returns Unit

            impl.execute(cmd).getOrThrow()

            coVerify { inventoryRepository.decrementItemQuantity(cmd.itemId, cmd.quantity) }
            coVerify { inventoryRepository.delete(cmd.itemId) }
        }
    }

    @Test
    fun `internalExecute throws when item not found after decrement`() {
        runBlocking {
            val cmd = com.khrix.domain.inventory.usecase.DecrementItemInventoryCommand(itemId = 2, quantity = 1)
            coEvery { inventoryRepository.decrementItemQuantity(cmd.itemId, cmd.quantity) } returns Unit
            coEvery { inventoryRepository.getByIdOrSku(cmd.itemId.toString()) } returns null

            val res = impl.execute(cmd)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}


