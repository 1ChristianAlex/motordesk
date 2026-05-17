package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class Password(val value: String) {

    val validation = Validation<Password> {
        Password::value  {
            minLength(8) hint "Password must be at least 8 characters long"
            pattern(passwordRegex) hint "Invalid password format"
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw IllegalArgumentException(validationResult.errors.joinToString { it.message })
        }
    }

    private val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$".toRegex()
}

