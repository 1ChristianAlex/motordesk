package com.khrix.domain.email.model

import com.khrix.testutils.sampleAddress
import com.khrix.testutils.sampleServiceOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ServiceOrderEmailMetadataTest {
    @Test
    fun `maps service order and client address to email metadata`() {
        val order = sampleServiceOrder()
        val address = sampleAddress()
        val metadata = ServiceOrderEmailMetadata(order, address)

        assertEquals(order.client.email.value, metadata.client.email)
        assertEquals(address.street, metadata.client.address?.street)
        assertNull(metadata.operator.address)
        assertEquals(order.totalPrice, metadata.totalAmount)
    }
}

class UserEmailMetadataTest {
    @Test
    fun `maps client identity`() {
        val metadata = ServiceOrderEmailMetadata(sampleServiceOrder(), sampleAddress())
        assertEquals(sampleServiceOrder().client.id, metadata.client.id)
    }
}

class AddressEmailMetadataTest {
    @Test
    fun `maps address fields`() {
        val address = sampleAddress()
        val metadata = ServiceOrderEmailMetadata(sampleServiceOrder(), address).client.address
        assertEquals(address.zipCode, metadata?.zipCode)
    }
}

class EmailQueueItemTest {
    @Test
    fun `retains queue delivery state`() {
        val item =
            EmailQueueItem(
                id = 1,
                recipient = "client@example.com",
                subject = "Subject",
                metadata = ServiceOrderEmailMetadata(sampleServiceOrder(), sampleAddress()),
                status = EmailStatus.PENDING,
                errorMessage = "",
                orderCode = "dasdd@#3",
                attempts = 0,
            )
        assertEquals(EmailStatus.PENDING, item.status)
        assertEquals(0, item.attempts)
    }
}

class EmailStatusTest {
    @Test
    fun `contains complete delivery lifecycle`() = assertEquals(3, EmailStatus.entries.size)
}
