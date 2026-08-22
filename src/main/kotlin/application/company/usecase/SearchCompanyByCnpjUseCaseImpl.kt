package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.port.repository.CompanyRepository
import com.khrix.domain.company.port.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.company.port.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.core.BaseUseCaseImpl

class SearchCompanyByCnpjUseCaseImpl(
    private val companyRepository: CompanyRepository,
) : BaseUseCaseImpl<SearchCompanyByCnpjUseCaseCommand, Company?>(),
    SearchCompanyByCnpjUseCase {
    override suspend fun internalExecute(command: SearchCompanyByCnpjUseCaseCommand): Company? = companyRepository.findByCnpj(command.cnpj)

    override suspend fun useCaseDescription(): String = "Hash password and create new user"
}
