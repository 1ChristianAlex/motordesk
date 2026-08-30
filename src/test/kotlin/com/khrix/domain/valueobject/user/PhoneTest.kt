package com.khrix.domain.valueobject.user

import com.khrix.domain.valueobject.user.Phone
import kotlin.test.Test
import kotlin.test.assertEquals

class PhoneTest {
    val instance = Phone("+1234567890")

    @Test
    fun `given PHONE instance should mask when mask method is called`() {
        assertEquals("(12) 34**-*890", instance.mask())
    }
}
