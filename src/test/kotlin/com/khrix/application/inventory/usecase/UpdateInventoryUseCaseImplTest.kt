package com.khrix.application.inventory.usecase

import com.khrix.application.inventory.UpdateInventoryUseCaseImpl
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

class UpdateInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = UpdateInventoryUseCaseImpl(inventoryRepository)

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
    fun `internalExecute calls update on repository`() {
        runTest {
            val item = sampleInventoryItem()
            coEvery { inventoryRepository.update(item.id, item) } returns Unit

            impl.execute(item).getOrThrow()
            coVerify { inventoryRepository.update(item.id, item) }
        }
    }
}
