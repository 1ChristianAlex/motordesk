package com.khrix.application.inventory.usecase

import com.khrix.application.inventory.CreateInventoryUseCaseImpl
import com.khrix.domain.inventory.repository.InventoryRepository
import com.khrix.testutils.sampleInventoryItem
import io.mockk.coEvery
import io.mockk.coVerify
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

class CreateInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = CreateInventoryUseCaseImpl(inventoryRepository)

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
            assertEquals("Create a new inventory item", impl.useCaseDescription())
        }
    }

    @Test
    fun `internalExecute calls repository createRead`() {
        runTest {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.createRead(item) } returns item

            val res = impl.execute(item)
            assertEquals(item, res.getOrThrow())
            coVerify { inventoryRepository.createRead(item) }
        }
    }
}
