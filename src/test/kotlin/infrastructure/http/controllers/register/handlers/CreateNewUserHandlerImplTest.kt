package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.security.TokenService
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.infrastructure.http.controllers.register.resources.dto.AddressDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.CompanyDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.CreateUserDto
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CreateNewUserHandlerImplTest {

    private val createNewUserUseCase = mockk<CreateNewUserUseCase>()
    private val verifyIsEmailAvailableUseCase: VerifyIsEmailAvailableUseCase = mockk()
    private val tokenService: TokenService = mockk()
    private val handler = CreateNewUserHandlerImpl(
        createNewUserUseCase = createNewUserUseCase,
        verifyIsEmailAvailableUseCase = verifyIsEmailAvailableUseCase,
        tokenService = tokenService
    )

    @BeforeTest
    fun setUp() {
        coEvery { verifyIsEmailAvailableUseCase.execute(any()) } returns Result.success(true)
        coEvery { tokenService.generate(any()) } returns Uuid.generateV4().toString()
    }

    @Test
    fun `should return 201 Created with user data on successful registration`() = runTest {
        // Arrange
        val clientRegisterDto = ClientRegisterDto(
            user = CreateUserDto(
                firstName = "Kaique",
                lastName = "Lopes",
                email = "augusto_lopes@indaiamidias.com.br",
                phone = "(47) 3733-9296",
                password = "test@123!",
                cpf = "216.417.800-96",
            ),
            address = AddressDto(
                street = "Rua das Flores",
                number = "123",
                complement = "Apto 4B",
                neighborhood = "Centro",
                city = "São Paulo",
                state = "SP",
                country = "Brazil",
                zipCode = "01310-100"
            ),
            company = CompanyDto(
                cnpj = "22.855.604/0001-52",
                name = "Test Company",
            )
        )

        val createdUserDto = clientRegisterDto.user.toDomain()

        coEvery { createNewUserUseCase.execute(any<CreateNewUserUseCaseCommand>()) } returns Result.success(
            createdUserDto
        )

        // Act
        val result = handler.handler(clientRegisterDto)

        // Assert
        assertEquals(HttpStatusCode.Created, result.status)
        assertNotNull(result.data)
        assertNull(result.errors)
    }

    @Test
    fun `should return BadRequest with validation errors when registration data is invalid`() = runTest {
        // Arrange
        val clientRegisterDto = ClientRegisterDto(
            user = CreateUserDto(
                firstName = "Kaique",
                lastName = "Lopes",
                email = "augusto_lopes@indaiamidias.com.br",
                phone = "33-9296",
                password = "test23",
                cpf = "216.417.800-96",
            ),
            address = AddressDto(
                street = "Rua das Flores",
                number = "123",
                complement = "Apto 4B",
                neighborhood = "Centro",
                city = "São Paulo",
                state = "SP",
                country = "Brazil",
                zipCode = "01310-100"
            ),
            company = CompanyDto(
                cnpj = "12345678000190",
                name = "Test Company",
            )
        )


        coEvery { createNewUserUseCase.execute(any<CreateNewUserUseCaseCommand>()) } returns Result.failure(
            Exception("Invalid registration data")
        )

        // Act
        val result = handler.handler(clientRegisterDto)

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertNull(result.data)
        assertNotNull(result.errors)
    }

}

