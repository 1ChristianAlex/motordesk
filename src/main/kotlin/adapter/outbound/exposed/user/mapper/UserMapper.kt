package com.khrix.adapter.outbound.exposed.user.mapper

import com.khrix.adapter.outbound.exposed.user.database.UserEntity
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun UserEntity.toModel(): User {
    val userId = id.value
    return suspendTransaction {
        User(
            id = userId,
            firstName = Name(firstName),
            lastName = Name(lastName),
            email = Email(email),
            password = Password.Hashed(password),
            phone = Phone(phone),
            cpf = CPF(cpf),
            isActive = isActive,
            addressId = address?.id?.value ?: 0,
            createdAt = createdAt,
            updatedAt = updatedAt,
            companyId = company?.id?.value,
            role = role,
        )
    }
}
