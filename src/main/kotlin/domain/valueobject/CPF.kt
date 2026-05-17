package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.pattern

data class CPF(val value: String) {
    val validation = Validation<CPF> {
        CPF::value {
            val cleaned = onlyDigits(value)

            validate("trimmedCPF", { cleaned }) {
                pattern("^\\d{11}$") hint "CPF must contain exactly 11 digits when non-digit characters are removed"
                constrain("validCPF") { !isValidCPF(cleaned) } hint "Invalid CPF number"
            }
            pattern("^[0-9.-\\s]+$") hint "CPF must contain only digits, dots, dashes or spaces"
        }
    }

    init {
        val validationResult = validation.validate(this)
        if (validationResult.errors.isNotEmpty()) {
            throw IllegalArgumentException(validationResult.errors.joinToString { it.message })
        }
    }

    private fun onlyDigits(input: String): String = input.filter { it.isDigit() }

    private fun isValidCPF(cpf: String): Boolean {
        if (cpf.all { it == cpf[0] }) return false

        try {
            val numbers = cpf.map { it.digitToInt() }

            val sum1 = (0 until 9).sumOf { i -> numbers[i] * (10 - i) }
            val r1 = sum1 % 11
            val d1 = if (r1 < 2) 0 else 11 - r1
            if (numbers[9] != d1) return false

            val sum2 = (0 until 10).sumOf { i -> numbers[i] * (11 - i) }
            val r2 = sum2 % 11
            val d2 = if (r2 < 2) 0 else 11 - r2
            return numbers[10] == d2
        } catch (ex: Exception) {
            return false
        }
    }
}
