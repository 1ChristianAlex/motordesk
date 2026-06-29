package com.khrix.domain.valueobject.user

import com.khrix.domain.valueobject.ValidationErrorResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NameTest {
    @Test
    fun `accepts a valid name`() = assertEquals("Chris", Name("Chris").value)

    @Test
    fun `rejects a one character name`() {
        assertFailsWith<ValidationErrorResult> { Name("C") }
    }
}
