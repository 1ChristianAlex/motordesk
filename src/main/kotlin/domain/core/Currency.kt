package com.khrix.domain.core

import java.text.NumberFormat
import java.util.Locale

fun Number.toCurrency(locale: Locale = Locale.forLanguageTag("pt-br")): String = NumberFormat.getCurrencyInstance(locale).format(this)
