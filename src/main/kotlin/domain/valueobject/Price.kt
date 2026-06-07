package com.khrix.domain.valueobject

import io.konform.validation.Validation
import java.math.BigDecimal

data class Price(val value: BigDecimal) {
    val validation = Validation.Companion<Price> {
        Price::value  {
            constrain("Price cannot be negative") { it >= BigDecimal.ZERO }
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }
}