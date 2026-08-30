package com.khrix.application.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCase
import com.khrix.domain.company.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.core.BaseUseCaseImpl

class SearchCompanyByCnpjUseCaseImpl(
    private val companyRepository: CompanyRepository,
) : SearchCompanyByCnpjUseCase, BaseUseCaseImpl<SearchCompanyByCnpjUseCaseCommand, Company?>() {
    override suspend fun internalExecute(command: SearchCompanyByCnpjUseCaseCommand): Company? {
        return companyRepository.findByCnpj(command.cnpj)
    }

    override suspend fun useCaseDescription(): String {
        return "Hash password and create new user"
    }
}
