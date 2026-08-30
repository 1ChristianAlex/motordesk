package com.khrix.application.inventory.usecase

import com.khrix.application.inventory.DeleteInventoryUseCaseImpl
import com.khrix.domain.inventory.repository.InventoryRepository
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

class DeleteInventoryUseCaseImplTest {
    private val inventoryRepository = mockk<InventoryRepository>()
    private val impl = DeleteInventoryUseCaseImpl(inventoryRepository)

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
            assert(impl.useCaseDescription().contains("Delete an existing inventory item"))
        }
    }

    @Test
    fun `internalExecute calls delete on repository`() {
        runTest {
            val id = 1
            coEvery { inventoryRepository.delete(id) } returns Unit

            impl.execute(id).getOrThrow()
            coVerify { inventoryRepository.delete(id) }
        }
    }
}
