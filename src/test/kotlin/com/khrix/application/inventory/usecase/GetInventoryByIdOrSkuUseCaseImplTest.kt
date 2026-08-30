package com.khrix.application.inventory.usecase

import com.khrix.application.inventory.GetInventoryByIdOrSkuUseCaseImpl
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.testutils.sampleInventoryItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetInventoryByIdOrSkuUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = GetInventoryByIdOrSkuUseCaseImpl(inventoryRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute returns item when found`() {
        runTest {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.getByIdOrSku("1") } returns item

            val res = impl.execute("1")
            assertEquals(item, res.getOrThrow())
        }
    }

    @Test
    fun `internalExecute throws when not found`() {
        runTest {
            coEvery { inventoryRepository.getByIdOrSku("missing") } returns null

            val res = impl.execute("missing")
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}
