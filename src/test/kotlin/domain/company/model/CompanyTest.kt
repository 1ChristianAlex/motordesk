package com.khrix.domain.company.model

import testutils.sampleCompany
import kotlin.test.Test
import kotlin.test.assertEquals

class CompanyTest {
    @Test
    fun `retains validated company data`() {
        val company = sampleCompany(7)
        assertEquals(7, company.id)
        assertEquals("12345678000195", company.cnpj.value)
    }
}
