package com.khrix.domain.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CompanyName

data class CreateNewCompanyUseCaseCommand(
    val name: CompanyName,
    val cnpj: CNPJ,
    val userId: Int,
)

interface CreateNewCompanyUseCase : BaseUseCase<CreateNewCompanyUseCaseCommand, Company>

sealed class CreateNewCompanyUseCaseError(
    message: String,
) : Exception(message) {
    data class CompanyAlreadyExists(
        val cnpj: CNPJ,
    ) : CreateNewCompanyUseCaseError("Company ${cnpj.value} already exists")
}
