package com.khrix.infrastructure.exposed.user.mapper

import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.*
import com.khrix.infrastructure.exposed.user.database.UserEntity
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun UserEntity.toModel(): User {
    val userId = id.value
    return suspendTransaction {
        User(
            id = userId,
            firstName = Name(firstName),
            lastName = Name(lastName),
            email = Email(email),
            password = Password(password),
            phone = Phone(phone),
            cpf = CPF(cpf),
            isActive = isActive,
            addressId = address?.id?.value ?: 0,
            createdAt = createdAt,
            updatedAt = updatedAt,
            companyId = company?.id?.value,
        )
    }
}