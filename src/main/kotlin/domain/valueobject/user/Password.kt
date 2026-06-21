package com.khrix.domain.valueobject.user

import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.minLength

sealed class Password(val value: String) {
    data class Hashed(private val _value: String) : Password(_value) {}

    data class Raw(private val _value: String) : Password(_value) {

        private fun validation() = Validation {
            Raw::_value  {
                minLength(8) hint "Password must be at least 8 characters long"
                constrain("Password need to have at least one special character") { checkSpecialChar(_value) }
                constrain("Password need to have at least one letter") { checkHasLetters(_value) }
                constrain("Password need to have at least one number") { checkHasNumbers(_value) }
            }
        }

        private fun checkSpecialChar(password: String): Boolean {
            val specialCharRegex = Regex("[!@#\$%^&*(),.?\":{}|<>]")
            return specialCharRegex.containsMatchIn(password)
        }

        private fun checkHasLetters(password: String): Boolean {
            val letterRegex = Regex("\\w")
            return letterRegex.containsMatchIn(password)
        }

        private fun checkHasNumbers(password: String): Boolean {
            val numbersRegex = Regex("\\d")
            return numbersRegex.containsMatchIn(password)
        }

        init {
            validateWith(validation())
        }
    }
}
