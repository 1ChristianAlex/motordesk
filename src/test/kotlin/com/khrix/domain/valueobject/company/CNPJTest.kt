package com.khrix.domain.valueobject.company

import com.khrix.domain.valueobject.company.CNPJ
import kotlin.test.Test
import kotlin.test.assertEquals

class CNPJTest {
    val instance = CNPJ("12345678000195")

    @Test
    fun `given CNPJ instance should mask when mask method is called`() {
        assertEquals("12.3**.***/****-95", instance.mask())
    }
}
