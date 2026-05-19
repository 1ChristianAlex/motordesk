package com.khrix.application.register.usecase

import com.khrix.domain.address.model.Address
import com.khrix.domain.address.repository.AddressRepository
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
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateNewUserUseCaseImplTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = spyk<PasswordHasherArgonImpl>()
    private val addressRepository = mockk<AddressRepository>()

    private val useCase = CreateNewUserUseCaseImpl(
        userRepository = userRepository,
        passwordHasher = passwordHasher,
        addressRepository = addressRepository
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
            createdAt = LocalDateTime(2026, 5, 18, 10, 0, 0),
            updatedAt = LocalDateTime(2026, 5, 18, 10, 0, 0),
            addressId = 0,
            isActive = true
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
            createdAt = LocalDateTime(2026, 5, 18, 10, 0, 0),
            updatedAt = LocalDateTime(2026, 5, 18, 10, 0, 0)
        )

        val command = CreateNewUserUseCaseCommand(user = user, address = address, cnpj = null, companyName = null)

        val userWithHashedPassword = user.copy(
            password = Password(hashedPassword),
            addressId = addressId
        )


        coEvery { userRepository.create(any()) } returns userId
        coEvery { addressRepository.create(address, userId) } returns addressId
        coEvery { userRepository.read(userId) } returns userWithHashedPassword

        // Act
        val result = useCase.execute(command).getOrNull()

        // Assert
        assertEquals(userWithHashedPassword, result)
        coVerify { passwordHasher.hash(plainPassword) }
        coVerify { userRepository.create(any()) }
        coVerify { addressRepository.create(address, userId) }
        coVerify { userRepository.read(userId) }
    }
}

