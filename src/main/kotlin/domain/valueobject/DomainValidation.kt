package com.khrix.domain.valueobject

import io.konform.validation.Validation

abstract class DomainValidation<Me> {
    abstract val validation: Validation<Me>

    init {
        val validationResult = validation.validate(this as Me)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }
}