@file:OptIn(ExperimentalUuidApi::class)

package com.khrix.infrastructure.exposed.user.repository

import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.uuid.ExperimentalUuidApi

class UserExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<UserEntity, User>(database), UserRepository {
    override suspend fun update(id: Int, data: User) {
        suspendedQuery {
            UserEntity.findByIdAndUpdate(id) {
                it.firstName = data.firstName
                it.lastName = data.lastName
                it.email = data.email
                it.password = data.password
                it.phone = data.phone
                it.cpf = data.cpf
                it.isActive = data.isActive
                it.address = AddressEntity[data.addressId]
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { UserEntity[id].delete() }
    }

    override suspend fun read(id: Int): User {
        return suspendedQuery {
            UserEntity[id].toModel()
        }
    }

    override suspend fun createRead(data: User): User {
        return suspendedQuery {
            UserEntity.new {
                firstName = data.firstName
                lastName = data.lastName
                email = data.email
                password = data.password
                phone = data.phone
                cpf = data.cpf
                isActive = data.isActive
                address = null
            }.toModel()
        }
    }
}