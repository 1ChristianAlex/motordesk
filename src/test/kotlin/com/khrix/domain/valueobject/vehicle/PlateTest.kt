package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.valueobject.ValidationErrorResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlateTest {
    @Test
    fun `accepts Mercosur format`() = assertEquals("ABC1D23", Plate("ABC1D23").value)

    @Test
    fun `rejects invalid format`() {
        assertFailsWith<ValidationErrorResult> { Plate("ABC-1234") }
    }
}
