package com.khrix.domain.valueobject.user

import com.khrix.domain.core.mask.maskString
import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

@JvmInline
value class Email(
    val value: String,
) {
    private fun validation() =
        Validation<Email> {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
            Email::value {
                minLength(5) hint "Email must be at least 5 characters long"
                pattern(emailRegex) hint "Invalid email format"
            }
        }

    fun mask(): String = maskString(value, 4, listOf('.', '@'))

    init {
        validateWith(validation())
    }
}
