package com.khrix.application.inventory.usecase

import com.khrix.application.inventory.GetInventoryByListIdOrSkuUseCaseImpl
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

class GetInventoryByListIdOrSkuUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = GetInventoryByListIdOrSkuUseCaseImpl(inventoryRepository)

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
            assert(impl.useCaseDescription().contains("Update an existing inventory item"))
        }
    }

    @Test
    fun `internalExecute returns items from repository`() {
        runTest {
            val items = listOf(sampleInventoryItem())
            coEvery { inventoryRepository.getByIdOrSku(listOf("1")) } returns items

            val res = impl.execute(listOf("1"))
            assertEquals(items, res.getOrThrow())
        }
    }
}
