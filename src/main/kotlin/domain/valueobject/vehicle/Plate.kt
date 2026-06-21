package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank
import io.konform.validation.constraints.pattern
import kotlinx.serialization.Serializable

@Serializable
data class Plate(val value: String) {
   private  val validation = Validation.Companion<Plate> {
        Plate::value  {
            notBlank() hint "Vehicle plate cannot be empty"
            pattern("^\\w{3}\\d\\w\\d{2}$".toRegex()) hint "Vehicle plate is not valid"
        }
    }

    init {
        validateWith(validation)
    }
}