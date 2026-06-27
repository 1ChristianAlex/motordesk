package com.khrix.domain.valueobject

import com.khrix.domain.valueobject.user.Name
import kotlin.test.Test
import kotlin.test.assertTrue

class ValidationErrorResultTest {
    @Test
    fun `validation errors identify their domain type`() {
        val error = runCatching { Name("x") }.exceptionOrNull() as ValidationErrorResult
        assertTrue(error.validationErrors.all { it.startsWith("Name - ") })
    }
}
