package com.khrix.application.email.usecase

import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.user.address.repository.AddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import testutils.sampleServiceOrder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateEmailQueueUseCaseImplTest {
    private val emailQueueRepository = mockk<EmailQueueRepository>()
    private val addressRepository = mockk<AddressRepository>()
    private val impl = CreateEmailQueueUseCaseImpl(emailQueueRepository, addressRepository)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `internalExecute creates email queue item`() {
        runTest {
            val so = sampleServiceOrder()
            val address = testutils.sampleAddress()
            coEvery { addressRepository.read(so.client.addressId) } returns address
            coEvery { emailQueueRepository.create(any()) } returns 1

            impl.execute(so).getOrThrow()
            coVerify { emailQueueRepository.create(any()) }
        }
    }

    @Test
    fun `internalExecute throws when address missing`() {
        runTest {
            val so = sampleServiceOrder()
            coEvery { addressRepository.read(so.client.addressId) } returns null

            val res = impl.execute(so)
            assertFailsWith<NoSuchElementException> { res.getOrThrow() }
        }
    }
}


