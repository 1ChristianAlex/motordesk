package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class Phone(val value: String) {

    val validation = Validation<Phone> {
        Phone::value  {
            minLength(5) hint "Phone must be at least 5 characters long"
            pattern(phoneRegex) hint "Invalid phone format"
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw IllegalArgumentException(validationResult.errors.joinToString { it.message })
        }
    }

    private val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()
}
