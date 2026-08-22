package com.khrix.domain.company.port.repository

import com.khrix.domain.company.model.Company
import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.valueobject.company.CNPJ

interface CompanyRepository :
    BaseRead<Company>,
    BaseUpdate<Company>,
    BaseCreate<Company>,
    BaseDelete,
    BaseCreateReturn<Company> {
    suspend fun findByCnpj(cnpj: CNPJ): Company?
}
