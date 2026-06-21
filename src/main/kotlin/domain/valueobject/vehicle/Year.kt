package com.khrix.domain.valueobject.vehicle

import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.core.validateWith
import io.konform.validation.Validation
import kotlinx.serialization.Serializable


@Serializable
@JvmInline
value class Year(val value: Int) {


    private fun validation() = Validation.Companion<Year> {
        val minYear = 1900
        Year::value  {
            constrain("Year must be between $minYear and the current year") {
                it in minYear..getCurrentUtcDateTime().year
            }
        }
    }

    init {
        validateWith(validation())
    }
}