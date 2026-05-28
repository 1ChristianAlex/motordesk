package com.khrix.infrastructure.exposed.company.mapper

import com.khrix.domain.company.model.Company
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.Name
import com.khrix.infrastructure.exposed.company.database.CompanyEntity

fun CompanyEntity.toModel(): Company = Company(
    id = id.value,
    name = Name(name),
    cnpj = CNPJ(cnpj),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

