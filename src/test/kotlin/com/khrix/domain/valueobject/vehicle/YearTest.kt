package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.valueobject.ValidationErrorResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class YearTest {
    @Test
    fun `accepts current year`() = assertEquals(getCurrentUtcDateTime().year, Year(getCurrentUtcDateTime().year).value)

    @Test
    fun `rejects years before 1900 and in the future`() {
        assertFailsWith<ValidationErrorResult> { Year(1899) }
        assertFailsWith<ValidationErrorResult> { Year(getCurrentUtcDateTime().year + 1) }
    }
}
