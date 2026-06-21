package com.khrix.application.register.usecase

import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import kotlinx.coroutines.runBlocking
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import testutils.sampleAddress
import testutils.sampleCompany
import testutils.sampleUser

class CreateNewUserUseCaseImplGeneratedTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val addressRepository = mockk<AddressRepository>()
    private val searchCompanyByCnpjUseCase = mockk<SearchCompanyByCnpjUseCase>()
    private val createNewCompanyUseCase = mockk<CreateNewCompanyUseCase>()

    private val impl = CreateNewUserUseCaseImpl(
        userRepository,
        passwordHasher,
        addressRepository,
        searchCompanyByCnpjUseCase,
        createNewCompanyUseCase
    )

    @Test
    fun `useCaseDescription returns expected string`() {
        runBlocking {
            assertEquals("Hash password and create new user", impl.useCaseDescription())
        }
    }

    @Test
    fun `internalExecute happy path creates user`() {
        runBlocking {
        val user = sampleUser()
        val address = sampleAddress()
        val company = sampleCompany()

        coEvery { passwordHasher.hash(any()) } returns "hashed"
        coEvery { addressRepository.create(any()) } returns 10
        coEvery { searchCompanyByCnpjUseCase.execute(any()) } returns Result.success(null)
        coEvery { createNewCompanyUseCase.execute(any()) } returns Result.success(company)
        coEvery { userRepository.create(any()) } returns 1
        coEvery { userRepository.read(1) } returns user

        val command = com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand(
            user = user,
            address = address,
            company = null
        )

            val res = impl.execute(command)

            val created = res.getOrThrow()
            assertEquals(user.email.value, created.email.value)

            coVerify { passwordHasher.hash(any()) }
            coVerify { addressRepository.create(any()) }
            coVerify { userRepository.create(any()) }
        }
    }
}

