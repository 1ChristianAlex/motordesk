package com.khrix.domain.valueobject

import io.konform.validation.ValidationResult
import kotlin.reflect.KClass

data class ValidationErrorResult(
    val validationErrors: List<String>
) : Exception()

fun ValidationResult<*>.toValidationError(kClass: KClass<*>): ValidationErrorResult {
    val allErrors = this.errors.map { "${kClass.simpleName} - ${it.message}" }
    return ValidationErrorResult(allErrors)
}