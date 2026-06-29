package com.khrix.domain.user.model

import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
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
    val role: Role,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun updatePassword(password: String): User = copy(password = Password.Hashed(password))

    fun updateAddress(addressId: Int): User = copy(addressId = addressId)

    fun updateCompany(companyId: Int?): User = copy(companyId = companyId)
}
