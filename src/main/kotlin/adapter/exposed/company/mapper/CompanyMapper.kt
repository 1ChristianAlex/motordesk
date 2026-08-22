package com.khrix.adapter.exposed.company.mapper

import com.khrix.adapter.exposed.company.database.CompanyEntity
import com.khrix.domain.company.model.Company
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CompanyName
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun CompanyEntity.toModel(): Company =
    suspendTransaction {
        Company(
            id = this@toModel.id.value,
            name = CompanyName(name),
            cnpj = CNPJ(cnpj),
            createdAt = createdAt,
            updatedAt = updatedAt,
            userId = user.id.value,
        )
    }
