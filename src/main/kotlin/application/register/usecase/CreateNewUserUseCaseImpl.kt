package com.khrix.application.register.usecase

import com.khrix.domain.address.repository.AddressRepository
import com.khrix.domain.company.model.Company
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.security.PasswordHasher
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
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
) : CreateNewUserUseCase,
    BaseUseCaseImpl<CreateNewUserUseCaseCommand, User>() {

    private suspend fun getOrCreateCompany(company: Company): Company {
        val companyExists =
            searchCompanyByCnpjUseCase.execute(SearchCompanyByCnpjUseCaseCommand(company.cnpj)).getOrNull()

        if (companyExists != null) {
            return companyExists
        }

        val newCompany =
            createNewCompanyUseCase.execute(
                CreateNewCompanyUseCaseCommand(cnpj = company.cnpj, name = company.name)
            ).getOrThrow()

        return newCompany
    }

    override suspend fun internalExecute(command: CreateNewUserUseCaseCommand): User {
        return coroutineScope {
            val hashedPass = async { passwordHasher.hash(command.user.password.value) }
            val addressId = async { addressRepository.create(command.address) }
            val userCompany = async {
                if (command.company != null) {
                    getOrCreateCompany(command.company)
                } else null
            }

            val userWithHashedPassword = command.user.updatePassword(hashedPass.await(), true)
                .updateAddress(addressId.await()).updateCompany(userCompany.await()?.id)

            val userId = userRepository.create(userWithHashedPassword)

            val user = userRepository.read(userId) ?: throw Exception("User not found after creation")

            user
        }
    }

    override suspend fun useCaseDescription(): String {
        return "Hash password and create new user"
    }
}
