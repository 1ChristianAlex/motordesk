package com.khrix.domain.core

import java.util.Locale
import kotlin.test.Test

class ToCurrencyTest {
    @Test
    fun `given a Number value return the currency value when given a Locale`() {
        val value = 1000.0

        val locale = Locale.forLanguageTag("pt-br")
        val currencyValue = value.toCurrency(locale)
        assert(currencyValue == "R$ 1.000,00")
    }
}
