package com.khrix.application.company.usecase

import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.valueobject.company.CNPJ
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchCompanyByCnpjUseCaseImplTest {
    private val companyRepository = mockk<CompanyRepository>()
    private val impl = SearchCompanyByCnpjUseCaseImpl(companyRepository)

    @Test
    fun `useCaseDescription returns expected string`() = runBlocking {
        assertEquals("Hash password and create new user", impl.useCaseDescription())
    }

    @Test
    fun `internalExecute returns company when found`() = runBlocking {
        val cnpj = CNPJ("12345678000195")
        val company = com.khrix.domain.company.model.Company(
            id = 1,
            name = com.khrix.domain.valueobject.user.Name("Company"),
            cnpj = cnpj,
            createdAt = kotlinx.datetime.LocalDateTime(2020,1,1,0,0),
            updatedAt = kotlinx.datetime.LocalDateTime(2020,1,1,0,0)
        )
        coEvery { companyRepository.findByCnpj(cnpj) } returns company

        val res = impl.execute(com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCaseCommand(cnpj))
        assertEquals(company, res.getOrThrow())
    }
}

