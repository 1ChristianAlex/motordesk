package com.khrix.infrastructure.exposed.user.mapper

import com.khrix.domain.user.model.User
import com.khrix.infrastructure.exposed.user.database.UserEntity
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun UserEntity.toModel(): User {
    val userId = id.value
    return suspendTransaction {
        User(
            id = userId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            phone = phone,
            cpf = cpf,
            isActive = isActive,
            addressId = address?.id?.value ?: 0
        )
    }
}