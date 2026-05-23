package com.khrix.domain.valueobject

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength

data class Password(val value: String, val bypass: Boolean = false) {

    val validation = Validation<Password> {
        Password::value  {
            minLength(8) hint "Password must be at least 8 characters long"
            constrain("Password need to have at least one special character") { checkSpecialChar(value) }
            constrain("Password need to have at least one letter") { checkHasLetters(value) }
            constrain("Password need to have at least one number") { checkHasNumbers(value) }
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
        if (!bypass) {
            val validationResult = validation.validate(this)
            if (validationResult.errors.isNotEmpty()) {
                throw validationResult.toValidationError(this::class)
            }
        }
    }
}

