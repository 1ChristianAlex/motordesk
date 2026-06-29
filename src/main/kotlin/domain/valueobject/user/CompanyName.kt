package com.khrix.domain.valueobject.user

import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength

@JvmInline
value class CompanyName(
    val value: String,
) {
    private fun validation() =
        Validation {
            CompanyName::value {
                validate("trimmed", { value.trim() }) {
                    minLength(2) hint "Must be at least 2 characters long"
                    maxLength(100) hint "Must not exceed 150 characters"
                }
            }
        }

    init {
        validateWith(validation())
    }
}
