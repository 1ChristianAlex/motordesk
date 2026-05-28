package com.khrix.domain.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.Name

data class CreateNewCompanyUseCaseCommand(
    val name: Name,
    val cnpj: CNPJ,
)

interface CreateNewCompanyUseCase : BaseUseCase<CreateNewCompanyUseCaseCommand, Company>

sealed class CreateNewCompanyUseCaseError(message: String) : Exception(message) {
    data class CompanyAlreadyExists(val cnpj: CNPJ) :
        CreateNewCompanyUseCaseError("Company ${cnpj.value} already exists")
}