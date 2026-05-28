package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.valueobject.toValidationError
import io.konform.validation.Validation

data class Year(val value: Int) {

    private val MIN_YEAR = 1900

    val validation = Validation.Companion<Year> {
        Year::value  {
            constrain("Year must be between $MIN_YEAR and the current year") {
                it in MIN_YEAR..getCurrentUtcDateTime().year
            }
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }
}