package com.khrix.domain.core

import com.khrix.domain.valueobject.toValidationError
import io.konform.validation.Validation

fun <T : Any> T.validateWith(validation: Validation<T>) {
    val result = validation.validate(this)

    if (result.errors.isNotEmpty()) {
        throw result.toValidationError(this::class)
    }
}
