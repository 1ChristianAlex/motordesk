package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.pattern

data class CNPJ(val value: String) {
    private val expectedCnpjLength = 14
    val validation = Validation<CNPJ> {
        CNPJ::value {
            val cleaned = normalize()
            validate("trimmedCNPJ", { cleaned }) {
                pattern("^\\d{$expectedCnpjLength}$") hint "CNPJ must contain exactly $expectedCnpjLength number digits"
                constrain("validCNPJ") { isValidCNPJ() } hint "Invalid CNPJ number"
            }
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError(this::class)
        }
    }


    fun normalize() = value.filter(Char::isDigit)

    private fun isValidCNPJ(): Boolean {
        with(normalize()) {
            try {
                if (length != expectedCnpjLength) {
                    return false
                }

                if (all { it == first() }) {
                    return false
                }

                val digits = map { it.digitToInt() }

                val firstWeights = listOf(
                    5, 4, 3, 2,
                    9, 8, 7, 6,
                    5, 4, 3, 2
                )

                val firstSum = digits
                    .take(12)
                    .zip(firstWeights)
                    .sumOf { (digit, weight) ->
                        digit * weight
                    }

                val firstRemainder = firstSum % 11

                val firstCheckDigit =
                    if (firstRemainder < 2) 0
                    else 11 - firstRemainder

                if (digits[12] != firstCheckDigit) {
                    return false
                }

                val secondWeights = listOf(
                    6, 5, 4, 3,
                    2, 9, 8, 7,
                    6, 5, 4, 3, 2
                )

                val secondSum = digits
                    .take(13)
                    .zip(secondWeights)
                    .sumOf { (digit, weight) ->
                        digit * weight
                    }

                val secondRemainder = secondSum % 11

                val secondCheckDigit =
                    if (secondRemainder < 2) 0
                    else 11 - secondRemainder

                return digits[13] == secondCheckDigit
            } catch (ex: Exception) {
                return false
            }
        }
    }
}
