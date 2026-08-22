package com.khrix.adapter.exposed.address.mapper

import com.khrix.adapter.exposed.address.database.AddressEntity
import com.khrix.domain.user.address.model.Address
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

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
            updatedAt = updatedAt,
        )
    }
}
