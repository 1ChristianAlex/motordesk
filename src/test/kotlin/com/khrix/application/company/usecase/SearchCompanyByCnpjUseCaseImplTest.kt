package com.khrix.application.company.usecase

import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.valueobject.company.CNPJ
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchCompanyByCnpjUseCaseImplTest {
    private val companyRepository = mockk<CompanyRepository>()
    private val impl = SearchCompanyByCnpjUseCaseImpl(companyRepository)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `useCaseDescription returns expected string`() = runTest {
        assertEquals("Hash password and create new user", impl.useCaseDescription())
    }

    @Test
    fun `internalExecute returns company when found`() = runTest {
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

