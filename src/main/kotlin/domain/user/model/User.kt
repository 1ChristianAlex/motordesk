package com.khrix.domain.user.model

import com.khrix.domain.valueobject.*
import kotlinx.datetime.LocalDateTime

data class User(
    val id: Int,
    val addressId: Int,
    val companyId: Int?,
    val firstName: Name,
    val lastName: Name,
    val email: Email,
    val password: Password,
    val phone: Phone,
    val cpf: CPF,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun updatePassword(password: String, bypass: Boolean = false): User {
        return copy(password = Password(password, bypass))
    }

    fun updateAddress(addressId: Int): User {
        return copy(addressId = addressId)
    }

    fun updateCompany(companyId: Int?): User {
        return copy(companyId = companyId)
    }
}