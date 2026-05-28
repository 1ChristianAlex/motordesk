package com.khrix.domain.company.repository

import com.khrix.domain.company.model.Company
import com.khrix.domain.core.*
import com.khrix.domain.valueobject.company.CNPJ

interface CompanyRepository :
    BaseRead<Company>,
    BaseUpdate<Company>,
    BaseCreate<Company>,
    BaseDelete,
    BaseCreateReturn<Company> {
    suspend fun findByCnpj(cnpj: CNPJ): Company?
}
