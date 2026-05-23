package com.khrix.domain.company.usecase

import com.khrix.domain.company.model.Company
import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.valueobject.CNPJ

data class SearchCompanyByCnpjUseCaseCommand(
    val cnpj: CNPJ,
)

interface SearchCompanyByCnpjUseCase : BaseUseCase<SearchCompanyByCnpjUseCaseCommand, Company?>

