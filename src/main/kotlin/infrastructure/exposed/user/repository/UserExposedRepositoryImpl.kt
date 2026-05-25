package com.khrix.infrastructure.exposed.user.repository

import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.valueobject.CPF
import com.khrix.domain.valueobject.Email
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import com.khrix.infrastructure.exposed.user.mapper.toModel
import org.jetbrains.exposed.v1.core.eq
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
        return suspendedQuery { createCleanUser(data).toModel() }
    }

    override suspend fun create(data: User): Int {
        return suspendedQuery { createCleanUser(data).id.value }
    }

    private fun createCleanUser(data: User): UserEntity {
        val user = UserEntity.new {
            firstName = data.firstName.value
            lastName = data.lastName.value
            email = data.email.value
            password = data.password.value
            phone = data.phone.normalize()
            cpf = data.cpf.normalize()
            isActive = true
            isEmailValid = false
            address = if (data.addressId > 0) AddressEntity[data.addressId] else null
        }

        return user
    }

    override suspend fun getByEmail(email: Email): User? {
        return suspendedQuery {
            UserEntity.find { UsersTable.email eq email.value }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getByCpf(cpf: CPF): User? {
        return suspendedQuery {
            UserEntity.find { UsersTable.cpf eq cpf.normalize() }.firstOrNull()?.toModel()
        }
    }
}