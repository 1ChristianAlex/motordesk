package com.khrix.domain.company.model

import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CompanyName
import kotlinx.datetime.LocalDateTime

data class Company(
    val id: Int,
    val name: CompanyName,
    val cnpj: CNPJ,
    val userId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
