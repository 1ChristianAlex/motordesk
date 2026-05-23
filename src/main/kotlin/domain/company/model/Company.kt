package com.khrix.domain.company.model

import com.khrix.domain.valueobject.CNPJ
import com.khrix.domain.valueobject.Name
import kotlinx.datetime.LocalDateTime

data class Company(
    val id: Int,
    val name: Name,
    val cnpj: CNPJ,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

