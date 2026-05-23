package com.khrix.infrastructure.exposed.company.mapper

import com.khrix.domain.company.model.Company
import com.khrix.domain.valueobject.CNPJ
import com.khrix.domain.valueobject.Name
import com.khrix.infrastructure.exposed.company.database.CompanyEntity

fun CompanyEntity.toModel(): Company = Company(
    id = id.value,
    name = Name(name),
    cnpj = CNPJ(cnpj),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

