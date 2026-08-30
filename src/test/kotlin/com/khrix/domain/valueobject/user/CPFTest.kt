package com.khrix.domain.valueobject.user

import com.khrix.domain.valueobject.user.CPF
import kotlin.test.Test
import kotlin.test.assertEquals

class CPFTest {
    val instance = CPF("114.154.800-36")

    @Test
    fun `given CPF instance should mask when mask method is called`() {
        assertEquals("114.***.***-36", instance.mask())
    }
}
