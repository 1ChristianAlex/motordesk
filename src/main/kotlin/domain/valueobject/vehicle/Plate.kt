package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.valueobject.toValidationError
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

data class Plate(val value: String) {
    val validation = Validation.Companion<Plate> {
        Plate::value  {
            notBlank() hint "Vehicle plate cannot be empty"
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }
}