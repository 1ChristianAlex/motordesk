package com.khrix.domain.valueobject

import io.konform.validation.ValidationResult

data class ValidationErrorResult(
    val validationErrors: List<String>
) : Exception()

fun ValidationResult<*>.toValidationError(): ValidationErrorResult {
    val allErrors = this.errors.map { it.message }
    return ValidationErrorResult(allErrors)
}