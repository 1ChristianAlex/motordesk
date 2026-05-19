package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class Phone(val value: String) {
    private val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()

    val validation = Validation<Phone> {
        Phone::value  {
            validate("only numbers allowed", { value.replace("\\D".toRegex(), "").trim() }) {
                minLength(5) hint "Phone must be at least 5 characters long"
                pattern(phoneRegex) hint "Invalid phone format"
            }
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError()
        }
    }

}
