package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.Name
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
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
    fun `useCaseDescription returns expected string`() =
        runTest {
            assertEquals("Hash password and create new user", impl.useCaseDescription())
        }

    @Test
    fun `internalExecute returns company when found`() =
        runTest {
            val cnpj = CNPJ("12345678000195")
            val company =
                Company(
                    id = 1,
                    name = Name("Company"),
                    cnpj = cnpj,
                    createdAt = LocalDateTime(2020, 1, 1, 0, 0),
                    updatedAt = LocalDateTime(2020, 1, 1, 0, 0),
                )
            coEvery { companyRepository.findByCnpj(cnpj) } returns company

            val res = impl.execute(SearchCompanyByCnpjUseCaseCommand(cnpj))
            assertEquals(company, res.getOrThrow())
        }
}
