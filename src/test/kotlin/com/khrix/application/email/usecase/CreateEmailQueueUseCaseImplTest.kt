package com.khrix.application.email.usecase

import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.application.core.coroutine.ApplicationScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import testutils.sampleServiceOrder
import kotlin.test.Test

class CreateEmailQueueUseCaseImplTest {
    private val emailQueueRepository = mockk<EmailQueueRepository>()
    private val addressRepository = mockk<AddressRepository>()
    private val scope = ApplicationScope()
    private val impl = CreateEmailQueueUseCaseImpl(emailQueueRepository, addressRepository, scope)

    @Test
    fun `internalExecute creates email queue item`() {
        runTest {
            val so = sampleServiceOrder()
            val address = testutils.sampleAddress()
            coEvery { addressRepository.read(so.client.addressId) } returns address
            coEvery { emailQueueRepository.create(any()) } returns 1

            impl.execute(so).getOrThrow()
            coVerify(timeout = 2_000) {
                emailQueueRepository.create(match {
                    it.recipient == so.client.email.value && it.metadata.client.address?.street == address.street
                })
            }
        }
    }

}


