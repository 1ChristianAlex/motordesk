package com.khrix.application.register.usecase

import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.SecurityHasher
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.testutils.sampleAddress
import com.khrix.testutils.sampleCompany
import com.khrix.testutils.sampleUser
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

class CreateNewUserUseCaseImplGeneratedTest {
    private val userRepository = mockk<UserRepository>()
    private val securityHasher = mockk<SecurityHasher>()
    private val addressRepository = mockk<AddressRepository>()
    private val searchCompanyByCnpjUseCase = mockk<SearchCompanyByCnpjUseCase>()
    private val createNewCompanyUseCase = mockk<CreateNewCompanyUseCase>()

    private val impl =
        CreateNewUserUseCaseImpl(
            userRepository,
            securityHasher,
            addressRepository,
            searchCompanyByCnpjUseCase,
            createNewCompanyUseCase,
        )

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `useCaseDescription returns expected string`() {
        runTest {
            assertEquals("Hash password and create new user", impl.useCaseDescription())
        }
    }

    @Test
    fun `internalExecute happy path creates user`() {
        runTest {
            val user = sampleUser()
            val address = sampleAddress()
            val company = sampleCompany()

            coEvery { securityHasher.hash(any()) } returns "hashed"
            coEvery { addressRepository.create(any()) } returns 10
            coEvery { searchCompanyByCnpjUseCase.execute(any()) } returns Result.success(null)
            coEvery { createNewCompanyUseCase.execute(any()) } returns Result.success(company)
            coEvery { userRepository.create(any()) } returns 1
            coEvery { userRepository.read(1) } returns user

            val command =
                CreateNewUserUseCaseCommand(
                    user = user,
                    address = address,
                    company = null,
                )

            val res = impl.execute(command)

            val created = res.getOrThrow()
            assertEquals(user.email.value, created.email.value)

            coVerify { securityHasher.hash(any()) }
            coVerify { addressRepository.create(any()) }
            coVerify { userRepository.create(any()) }
        }
    }
}
