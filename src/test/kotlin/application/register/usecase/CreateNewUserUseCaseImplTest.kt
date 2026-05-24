package com.khrix.application.register.usecase

import com.khrix.domain.address.model.Address
import com.khrix.domain.address.repository.AddressRepository
import com.khrix.domain.company.model.Company
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.valueobject.*
import com.khrix.infrastructure.security.PasswordHasherArgonImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateNewUserUseCaseImplTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = spyk<PasswordHasherArgonImpl>()
    private val addressRepository = mockk<AddressRepository>()
    private val searchCompanyByCnpjUseCase = mockk<SearchCompanyByCnpjUseCase>(relaxed = true)
    private val createNewCompanyUseCase = mockk<CreateNewCompanyUseCase>(relaxed = true)

    private val useCase = CreateNewUserUseCaseImpl(
        userRepository = userRepository,
        passwordHasher = passwordHasher,
        addressRepository = addressRepository,
        searchCompanyByCnpjUseCase = searchCompanyByCnpjUseCase,
        createNewCompanyUseCase = createNewCompanyUseCase
    )

    @Test
    fun `should hash password and create user with address successfully`() = runTest {
        // Arrange
        val plainPassword = "password@123"
        val hashedPassword = "hashed_password_hash"
        val userId = 1
        val addressId = 1

        val user = User(
            id = 0,
            firstName = Name("John Doe"),
            lastName = Name("Smith"),
            email = Email("john@example.com"),
            phone = Phone("1234567890"),
            password = Password(plainPassword),
            cpf = CPF("12345678901"),
            createdAt = getCurrentUtcDateTime(),
            updatedAt = getCurrentUtcDateTime(),
            addressId = 0,
            isActive = true,
            companyId = null
        )

        val address = Address(
            id = null,
            street = "Main Street",
            number = "123",
            neighborhood = "Downtown",
            city = "São Paulo",
            state = "SP",
            country = "Brazil",
            zipCode = "01310-100",
            createdAt = getCurrentUtcDateTime(),
            updatedAt = getCurrentUtcDateTime()
        )
        val company = Company(
            createdAt = getCurrentUtcDateTime(),
            updatedAt = getCurrentUtcDateTime(),
            cnpj = CNPJ("22.855.604/0001-52"),
            name = Name("Example Company"),
            id = 0
        )

        val command = CreateNewUserUseCaseCommand(user = user, address = address, company)

        val userWithHashedPassword = user.copy(
            password = Password(hashedPassword), addressId = addressId, companyId = 1
        )

        val companyCommand = CreateNewCompanyUseCaseCommand(cnpj = company.cnpj, name = company.name)
        coEvery { searchCompanyByCnpjUseCase.execute(any()) } returns Result.success(null)
        coEvery { userRepository.create(any()) } returns userId
        coEvery { addressRepository.create(address) } returns addressId
        coEvery { userRepository.read(userId) } returns userWithHashedPassword
        coEvery { createNewCompanyUseCase.execute(companyCommand) } returns Result.success(company.copy(id = 1))

        // Act
        val result = useCase.execute(command).getOrNull()

        // Assert
        assertEquals(userWithHashedPassword, result)
        coVerify { passwordHasher.hash(plainPassword) }
        coVerify { userRepository.create(any()) }
        coVerify { addressRepository.create(address) }
        coVerify { userRepository.read(userId) }
    }
}

