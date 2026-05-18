package com.khrix.infrastructure.exposed.user.repository

import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.mapper.toModel
import org.jetbrains.exposed.v1.jdbc.Database

class UserExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<UserEntity, User>(database), UserRepository {
    override suspend fun update(id: Int, data: User) {
        suspendedQuery {
            UserEntity.findByIdAndUpdate(id) {
                it.firstName = data.firstName.value
                it.lastName = data.lastName.value
                it.email = data.email.value
                it.password = data.password.value
                it.phone = data.phone.value
                it.cpf = data.cpf.value
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
                firstName = data.firstName.value
                lastName = data.lastName.value
                email = data.email.value
                password = data.password.value
                phone = data.phone.value
                cpf = data.cpf.value
                isActive = data.isActive
                address = null
            }.toModel()
        }
    }

    override suspend fun create(data: User): Int {
        return suspendedQuery {
            UserEntity.new {
                firstName = data.firstName.value
                lastName = data.lastName.value
                email = data.email.value
                password = data.password.value
                phone = data.phone.value
                cpf = data.cpf.value
                isActive = data.isActive
                address = null
            }.id.value
        }
    }
}