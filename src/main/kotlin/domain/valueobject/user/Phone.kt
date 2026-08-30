package com.khrix.domain.valueobject.user

import com.khrix.domain.core.mask.maskString
import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

@JvmInline
value class Phone(
    val value: String,
) {
    private fun validation() =
        Validation.Companion<Phone> {
            val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()
            Phone::value {
                validate("only numbers allowed", { value.replace("\\D".toRegex(), "").trim() }) {
                    minLength(5) hint "Phone must be at least 5 characters long"
                    pattern(phoneRegex) hint "Invalid phone format"
                }
            }
        }

    fun normalize(): String = value.filter { it.isDigit() }

    fun format(): String {
        val digits = normalize()

        return when (digits.length) {
            13 -> {
                val regex = Regex("""^(\d{2})(\d{2})(\d{5})(\d{4})$""")
                regex.replace(digits, "+$1 ($2) $3-$4")
            }

            12 -> {
                val regex = Regex("""^(\d{2})(\d{2})(\d{4})(\d{4})$""")
                regex.replace(digits, "+$1 ($2) $3-$4")
            }

            11 -> {
                val regex = Regex("""^(\d{2})(\d{5})(\d{4})$""")
                regex.replace(digits, "($1) $2-$3")
            }

            10 -> {
                val regex = Regex("""^(\d{2})(\d{4})(\d{4})$""")
                regex.replace(digits, "($1) $2-$3")
            }

            else -> {
                digits
            }
        }
    }

    fun mask(): String =
        format().run {
            maskString(this, 2, listOf('(', ')', '-', ' '))
        }

    init {
        validateWith(validation())
    }
}
