package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class Name(val value: String) {

    val validation = Validation<Name> {
        Name::value {
            minLength(2) hint "Must be at least 2 characters long"
            maxLength(100) hint "Must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s'-]+$") hint "Can only contain letters, spaces, hyphens, and apostrophes"
        }
    }

    init {
        val trimmed = value.trim()

        val nameToValidate = this.copy(value = trimmed)
        val validationResult = validation.validate(nameToValidate)
        if (validationResult.errors.isNotEmpty()) {
            throw IllegalArgumentException(validationResult.errors.joinToString { it.message })
        }
    }
}

