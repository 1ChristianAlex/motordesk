package com.khrix.domain.user.model

import com.khrix.domain.valueobject.*
import kotlinx.datetime.LocalDateTime

data class User(
    val id: Int,
    val addressId: Int,
    val firstName: Name,
    val lastName: Name,
    val email: Email,
    val password: Password,
    val phone: Phone,
    val cpf: CPF,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)