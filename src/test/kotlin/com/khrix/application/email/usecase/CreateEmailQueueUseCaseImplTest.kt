package com.khrix.application.email.usecase

import com.khrix.application.core.coroutine.ApplicationScope
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.testutils.sampleAddress
import com.khrix.testutils.sampleServiceOrder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CreateEmailQueueUseCaseImplTest {
    private val emailQueueRepository = mockk<EmailQueueRepository>()
    private val addressRepository = mockk<AddressRepository>()
    private val scope = ApplicationScope()
    private val eventPublisher = mockk<EventPublisher>(relaxed = true)
    private val impl =
        CreateEmailQueueUseCaseImpl(
            emailQueueRepository = emailQueueRepository,
            addressRepository = addressRepository,
            scope = scope,
            eventPublisher = eventPublisher,
        )

    @Test
    fun `internalExecute creates email queue item`() {
        runTest {
            val so = sampleServiceOrder()
            val address = sampleAddress()
            coEvery { addressRepository.read(so.client.addressId) } returns address
            coEvery { emailQueueRepository.create(any()) } returns 1

            impl.execute(so).getOrThrow()
            coVerify(timeout = 2_000) {
                emailQueueRepository.create(
                    match {
                        it.recipient == so.client.email.value && it.metadata.client.address
                            ?.street == address.street
                    },
                )
            }
        }
    }
}
