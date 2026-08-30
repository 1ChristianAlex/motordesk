package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.port.repository.CompanyRepository
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCaseError
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.core.getCurrentUtcDateTime

class CreateNewCompanyUseCaseImpl(
    private val companyRepository: CompanyRepository,
) : BaseUseCaseImpl<CreateNewCompanyUseCaseCommand, Company>(),
    CreateNewCompanyUseCase {
    override suspend fun internalExecute(command: CreateNewCompanyUseCaseCommand): Company {
        val companyExists = companyRepository.findByCnpj(command.cnpj) != null

        if (companyExists) {
            throw CreateNewCompanyUseCaseError.CompanyAlreadyExists(command.cnpj)
        }

        val now = getCurrentUtcDateTime()
        val company =
            companyRepository.createRead(
                Company(
                    id = 0,
                    name = command.name,
                    cnpj = command.cnpj,
                    createdAt = now,
                    updatedAt = now,
                    userId = command.userId,
                ),
            )

        return company
    }

    override suspend fun useCaseDescription(): String = "Create new company"
}
