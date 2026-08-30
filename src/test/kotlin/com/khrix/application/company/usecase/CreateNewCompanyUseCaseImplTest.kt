package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.port.repository.CompanyRepository
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CompanyName
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `useCaseDescription returns expected string`() =
        runTest {
            assertEquals("Create new company", impl.useCaseDescription())
        }

    @Test
    fun `internalExecute creates new company when not exists`() =
        runTest {
            val command =
                CreateNewCompanyUseCaseCommand(
                    cnpj = CNPJ("12345678000195"),
                    name = CompanyName("Company"),
                    userId = 0,
                )

            val created =
                Company(
                    id = 1,
                    name = command.name,
                    cnpj = command.cnpj,
                    createdAt = LocalDateTime(2020, 1, 1, 0, 0),
                    updatedAt = LocalDateTime(2020, 1, 1, 0, 0),
                    userId = 0,
                )

            coEvery { companyRepository.findByCnpj(command.cnpj) } returns null
            coEvery { companyRepository.createRead(any()) } returns created

            val res = impl.execute(command)
            assertEquals(created, res.getOrThrow())
            coVerify { companyRepository.createRead(any()) }
        }
}
