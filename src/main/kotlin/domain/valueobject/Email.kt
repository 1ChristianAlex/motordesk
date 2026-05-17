package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class Email(val value: String) {

    val validEmail = Validation<Email> {
        Email::value  {
            minLength(5) hint "Email must be at least 5 characters long"
            pattern(emailRegex) hint "Invalid email format"
        }
    }

    init {
        val validationResult = validEmail.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw IllegalArgumentException(validationResult.errors.joinToString { it.message })
        }
    }

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

}
