package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import testutils.sampleInventoryItem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DecrementItemInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = DecrementItemInventoryUseCaseImpl(inventoryRepository)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `useCaseDescription returns expected string`() {
        runTest {
            assert(impl.useCaseDescription().contains("Safely decrement"))
        }
    }

    @Test
    fun `internalExecute deletes item when quantity becomes zero or less`() {
        runTest {
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
        runTest {
            val cmd = com.khrix.domain.inventory.usecase.DecrementItemInventoryCommand(itemId = 2, quantity = 1)
            coEvery { inventoryRepository.decrementItemQuantity(cmd.itemId, cmd.quantity) } returns Unit
            coEvery { inventoryRepository.getByIdOrSku(cmd.itemId.toString()) } returns null

            val res = impl.execute(cmd)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}


