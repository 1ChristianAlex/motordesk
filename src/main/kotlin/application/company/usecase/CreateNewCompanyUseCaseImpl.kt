package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.company.usecase.CreateNewCompanyUseCase
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.usecase.CreateNewCompanyUseCaseError
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.core.getCurrentUtcDateTime

class CreateNewCompanyUseCaseImpl(
    private val companyRepository: CompanyRepository,
) : CreateNewCompanyUseCase, BaseUseCaseImpl<CreateNewCompanyUseCaseCommand, Company>() {
    override suspend fun internalExecute(command: CreateNewCompanyUseCaseCommand): Company {
        val companyExists = companyRepository.findByCnpj(command.cnpj) != null

        if (companyExists) {
            throw CreateNewCompanyUseCaseError.CompanyAlreadyExists(command.cnpj)
        }

        val now = getCurrentUtcDateTime()
        val company = companyRepository.createRead(
            Company(
                id = 0,
                name = command.name,
                cnpj = command.cnpj,
                createdAt = now,
                updatedAt = now,
            )
        )

        return company
    }

    override suspend fun useCaseDescription(): String {
        return "Create new company"
    }
}
