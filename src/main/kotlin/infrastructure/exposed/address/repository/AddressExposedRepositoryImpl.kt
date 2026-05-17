@file:OptIn(ExperimentalUuidApi::class)

package com.khrix.infrastructure.exposed.address.repository

import com.khrix.domain.address.model.Address
import com.khrix.domain.address.repository.AddressRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.address.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.uuid.ExperimentalUuidApi

class AddressExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<AddressEntity, Address>(database), AddressRepository {
    override suspend fun createRead(data: Address, userId: Int): Address {
        return suspendedQuery {
            AddressEntity.new {
                street = data.street
                number = data.number
                complement = data.complement
                neighborhood = data.neighborhood
                city = data.city
                state = data.state
                country = data.country
                zipCode = data.zipCode
            }.toModel()
        }
    }

    override suspend fun read(id: Int): Address {
        return suspendedQuery {
            AddressEntity[id].toModel()
        }
    }

    override suspend fun update(id: Int, data: Address) {
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
}