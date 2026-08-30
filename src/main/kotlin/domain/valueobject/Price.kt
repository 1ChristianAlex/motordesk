package com.khrix.domain.valueobject

import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import com.khrix.domain.core.toCurrency
import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Locale

@Serializable
@JvmInline
value class Price(
    @Serializable(with = DecimalAsStringSerializer::class) val value: BigDecimal,
) {
    private fun validation() =
        Validation.Companion<Price> {
            Price::value {
                constrain("Price cannot be negative") { it >= BigDecimal.ZERO }
            }
        }

    fun toCurrency() = value.toCurrency(Locale.forLanguageTag("pt-br"))

    init {
        validateWith(validation())
    }
}
