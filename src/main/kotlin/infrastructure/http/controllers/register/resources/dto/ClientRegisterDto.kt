package com.khrix.infrastructure.http.controllers.register.resources.dto

import com.khrix.domain.address.model.Address
import com.khrix.domain.company.model.Company
import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.*
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class ClientRegisterDto(
    val user: CreateUserDto,
    val address: AddressDto,
    val company: CompanyDto?
)

@Serializable
data class CompanyDto(
    val cnpj: String,
    val name: String,
) {
    fun toDomain(): Company {
        val now = getCurrentUtcDateTime()
        return Company(
            id = 0,
            cnpj = CNPJ(this.cnpj),
            name = Name(this.name),
            createdAt = now,
            updatedAt = now
        )
    }
}

@Serializable
data class CreateUserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String,
    val cpf: String,
) {
    fun toDomain(): User {
        val now = getCurrentUtcDateTime()
        return User(
            id = 0,
            addressId = 0,
            firstName = Name(this.firstName),
            lastName = Name(this.lastName),
            email = Email(this.email),
            password = Password(this.password),
            phone = Phone(this.phone),
            cpf = CPF(this.cpf),
            isActive = false,
            createdAt = now,
            updatedAt = now,
            companyId = null
        )
    }
}

@Serializable
data class AddressDto(
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String,
) {
    fun toDomain(): Address {
        val now = getCurrentUtcDateTime()

        return Address(
            street = this.street,
            number = this.number,
            complement = this.complement,
            neighborhood = this.neighborhood,
            city = this.city,
            state = this.state,
            country = this.country,
            zipCode = this.zipCode,
            createdAt = now,
            updatedAt = now
        )
    }
}

@Serializable
data class RegisterOutputDto(
    val token: String,
    val user: UserOutputDto
) {
}