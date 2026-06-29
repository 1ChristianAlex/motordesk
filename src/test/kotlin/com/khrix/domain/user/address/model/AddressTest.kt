package com.khrix.domain.user.address.model

import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.testutils.sampleAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddressTest {
    @Test
    fun `accepts valid address`() = assertEquals("Street", sampleAddress().street)

    @Test
    fun `rejects an invalid street`() {
        assertFailsWith<ValidationErrorResult> { sampleAddress().copy(street = "1") }
    }
}
