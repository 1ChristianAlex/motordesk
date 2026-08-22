package com.khrix.adapter.exposed.address.repository

import com.khrix.adapter.exposed.BaseExposedRepository
import com.khrix.adapter.exposed.address.database.AddressEntity
import com.khrix.adapter.exposed.address.mapper.toModel
import com.khrix.domain.user.address.model.Address
import com.khrix.domain.user.address.repository.AddressRepository
import org.jetbrains.exposed.v1.jdbc.Database

class AddressExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<AddressEntity, Address>(database),
    AddressRepository {
    override suspend fun createRead(data: Address): Address =
        suspendedQuery {
            createNewAddress(data).toModel()
        }

    override suspend fun read(id: Int): Address =
        suspendedQuery {
            AddressEntity[id].toModel()
        }

    override suspend fun update(
        id: Int,
        data: Address,
    ) {
        suspendedQuery {
            AddressEntity.findByIdAndUpdate(id) {
                it.street = data.street
                it.number = data.number
                it.complement = data.complement
                it.neighborhood = data.neighborhood
                it.city = data.city
                it.state = data.state
                it.country = data.country
                it.zipCode = data.zipCode
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { AddressEntity[id].delete() }
    }

    override suspend fun create(data: Address): Int =
        suspendedQuery {
            createNewAddress(data).id.value
        }

    private fun createNewAddress(data: Address) =
        AddressEntity.new {
            street = data.street
            number = data.number
            complement = data.complement
            neighborhood = data.neighborhood
            city = data.city
            state = data.state
            country = data.country
            zipCode = data.zipCode
        }
}
