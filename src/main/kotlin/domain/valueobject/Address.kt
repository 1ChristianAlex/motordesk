package com.khrix.domain.valueobject

import com.khrix.domain.address.model.Address
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

data class AddressValueObject(
    val address: Address,
) {

    val validation = Validation<Address> {
        Address::street {
            minLength(3) hint "Street must be at least 3 characters long"
            maxLength(100) hint "Street must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s.'-]+$") hint "Street can only contain letters, spaces, dots, hyphens, and apostrophes"
        }
        Address::number {
            minLength(1) hint "Number must not be empty"
            maxLength(20) hint "Number must not exceed 20 characters"
            pattern("^[0-9a-zA-Z/-]+$") hint "Number can only contain digits, letters, hyphens, and slashes"
        }
        Address::complement ifPresent {
            minLength(3) hint "Complement must be at least 3 characters long"
            maxLength(100) hint "Complement must not exceed 100 characters"
        }
        Address::neighborhood {
            minLength(2) hint "Neighborhood must be at least 2 characters long"
            maxLength(100) hint "Neighborhood must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s.'-]+$") hint "Neighborhood can only contain letters, spaces, dots, hyphens, and apostrophes"
        }
        Address::city {
            minLength(2) hint "City must be at least 2 characters long"
            maxLength(100) hint "City must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s.'-]+$") hint "City can only contain letters, spaces, dots, hyphens, and apostrophes"
        }
        Address::state {
            minLength(2) hint "State must be at least 2 characters long"
            maxLength(100) hint "State must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s.'-]+$") hint "State can only contain letters, spaces, dots, hyphens, and apostrophes"
        }
        Address::country {
            minLength(2) hint "Country must be at least 2 characters long"
            maxLength(100) hint "Country must not exceed 100 characters"
            pattern("^[a-zA-ZáéíóúãõçÁÉÍÓÚÃÕÇ\\s.'-]+$") hint "Country can only contain letters, spaces, dots, hyphens, and apostrophes"
        }
        Address::zipCode {
            minLength(3) hint "ZipCode must be at least 3 characters long"
            maxLength(20) hint "ZipCode must not exceed 20 characters"
            pattern("^[0-9a-zA-Z\\s-]+$") hint "ZipCode can only contain digits, letters, spaces, and hyphens"
        }
    }

    fun validate() {
        val validationResult = validation.validate(address)
        if (validationResult.errors.isNotEmpty()) {
            throw validationResult.toValidationError()
        }
    }
}

