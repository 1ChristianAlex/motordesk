package com.khrix.domain.valueobject

import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import io.konform.validation.Validation
import kotlinx.serialization.Serializable
import java.math.BigDecimal


@Serializable
data class Price(@Serializable(with = DecimalAsStringSerializer::class) val value: BigDecimal) {
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