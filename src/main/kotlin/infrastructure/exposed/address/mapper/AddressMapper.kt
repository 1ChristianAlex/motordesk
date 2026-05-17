package com.khrix.infrastructure.exposed.address.mapper

import com.khrix.domain.address.model.Address
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun Address.toEntity(): AddressEntity {
    val addressId = id ?: throw IllegalArgumentException("Address ID cannot be null when converting to entity")

    return suspendTransaction {
        AddressEntity[addressId]
    }
}

suspend fun AddressEntity.toModel(): Address {
    val addressId = id.value
    return suspendTransaction {
        Address(
            id = addressId,
            street = street,
            number = number,
            complement = complement,
            neighborhood = neighborhood,
            city = city,
            state = state,
            country = country,
            zipCode = zipCode,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}