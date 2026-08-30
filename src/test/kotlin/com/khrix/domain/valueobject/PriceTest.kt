package com.khrix.domain.valueobject

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PriceTest {
    @Test
    fun `accepts zero and positive prices`() = assertEquals(BigDecimal.ZERO, Price(BigDecimal.ZERO).value)

    @Test
    fun `rejects negative prices`() {
        assertFailsWith<ValidationErrorResult> { Price(BigDecimal("-0.01")) }
    }
}
