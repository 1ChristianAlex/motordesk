package com.khrix.application.inventory

import com.khrix.domain.inventory.repository.InventoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import testutils.sampleInventoryItem

class GetInventoryByIdOrSkuUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = GetInventoryByIdOrSkuUseCaseImpl(inventoryRepository)

    @Test
    fun `internalExecute returns item when found`() {
        runBlocking {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.getByIdOrSku("1") } returns item

            val res = impl.execute("1")
            assertEquals(item, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runBlocking {
            coEvery { inventoryRepository.getByIdOrSku("missing") } returns null

            val res = impl.execute("missing")
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}


