package com.khrix.domain.valueobject.user

import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

@JvmInline
value class Name(
    val value: String,
) {
    private fun validation() =
        Validation<Name> {
            Name::value {
                validate("trimmed", { value.trim() }) {
                    minLength(2) hint "Must be at least 2 characters long"
                    maxLength(100) hint "Must not exceed 100 characters"
                    pattern("^\\w+\\s?\\w+$".toRegex()) hint "Can only contain letters, spaces, hyphens, and apostrophes"
                }
            }
        }

    init {
        validateWith(validation())
    }
}
