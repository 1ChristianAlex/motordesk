package com.khrix.application.company.usecase

import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.Name
import io.mockk.coEvery
import io.mockk.coVerify
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

class CreateNewCompanyUseCaseImplTest {
    private val companyRepository = mockk<CompanyRepository>()
    private val impl = CreateNewCompanyUseCaseImpl(companyRepository)

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
        assertEquals("Create new company", impl.useCaseDescription())
    }

    @Test
    fun `internalExecute creates new company when not exists`() = runTest {
        val command = com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand(
            cnpj = CNPJ("12345678000195"),
            name = Name("Company")
        )

        val created = com.khrix.domain.company.model.Company(
            id = 1,
            name = command.name,
            cnpj = command.cnpj,
            createdAt = kotlinx.datetime.LocalDateTime(2020, 1, 1, 0, 0),
            updatedAt = kotlinx.datetime.LocalDateTime(2020, 1, 1, 0, 0)
        )

        coEvery { companyRepository.findByCnpj(command.cnpj) } returns null
        coEvery { companyRepository.createRead(any()) } returns created

        val res = impl.execute(command)
        assertEquals(created, res.getOrThrow())
        coVerify { companyRepository.createRead(any()) }
    }
}

