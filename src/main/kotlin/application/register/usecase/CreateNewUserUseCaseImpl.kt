package com.khrix.application.register.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.address.repository.AddressRepository
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CreateNewUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val addressRepository: AddressRepository,
    private val searchCompanyByCnpjUseCase: SearchCompanyByCnpjUseCase,
    private val createNewCompanyUseCase: CreateNewCompanyUseCase,
) : BaseUseCaseImpl<CreateNewUserUseCaseCommand, User>(),
    CreateNewUserUseCase {
    private suspend fun getOrCreateCompany(company: Company): Company {
        val companyExists =
            searchCompanyByCnpjUseCase.execute(SearchCompanyByCnpjUseCaseCommand(company.cnpj)).getOrNull()

        if (companyExists != null) {
            return companyExists
        }

        val newCompany =
            createNewCompanyUseCase
                .execute(
                    CreateNewCompanyUseCaseCommand(
                        cnpj = company.cnpj,
                        name = company.name,
                        userId = company.userId,
                    ),
                ).getOrThrow()

        return newCompany
    }

    override suspend fun internalExecute(command: CreateNewUserUseCaseCommand): User =
        coroutineScope {
            val hashedPass = async { passwordHasher.hash(command.user.password.value) }
            val addressId = async { addressRepository.create(command.address) }

            val userWithHashedPassword =
                command.user
                    .updatePassword(hashedPass.await())
                    .updateAddress(addressId.await())

            val userId = userRepository.create(userWithHashedPassword)

            val user = userRepository.read(userId) ?: throw NoSuchElementException("User not found after creation")

            if (command.company != null) {
                val company = getOrCreateCompany(command.company.copy(userId = userId))
                user.updateCompany(company.id)
            } else {
                user
            }
        }

    override suspend fun useCaseDescription(): String = "Hash password and create new user"
}
