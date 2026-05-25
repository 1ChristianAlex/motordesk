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
    fun updateFull(
        addressId: Int?,
        companyId: Int?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
        phone: String?,
        cpf: String?,
        isActive: Boolean?,
    ): User {
        return copy(
            addressId = addressId ?: this.addressId,
            companyId = companyId ?: this.companyId,
            firstName = Name(firstName ?: this.firstName.value),
            lastName = Name(lastName ?: this.lastName.value),
            email = Email(email ?: this.email.value),
            password = Password(password ?: this.password.value, true),
            phone = Phone(phone ?: this.phone.value),
            cpf = CPF(cpf ?: this.cpf.value),
            isActive = isActive ?: this.isActive,
        )
    }

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