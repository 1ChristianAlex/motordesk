package com.khrix.adapter.exposed.user.repository

import com.khrix.adapter.exposed.BaseExposedRepository
import com.khrix.adapter.exposed.address.database.AddressEntity
import com.khrix.adapter.exposed.company.database.CompanyEntity
import com.khrix.adapter.exposed.company.database.CompanyTable
import com.khrix.adapter.exposed.user.database.UserEntity
import com.khrix.adapter.exposed.user.database.UsersTable
import com.khrix.adapter.exposed.user.mapper.toModel
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

class UserExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<UserEntity, User>(database),
    UserRepository {
    override suspend fun update(
        id: Int,
        data: User,
    ) {
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
                it.role = data.role
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { UserEntity[id].delete() }
    }

    override suspend fun read(id: Int): User =
        suspendedQuery {
            UserEntity[id].toModel()
        }

    override suspend fun createRead(data: User): User = suspendedQuery { createCleanUser(data).toModel() }

    override suspend fun create(data: User): Int = suspendedQuery { createCleanUser(data).id.value }

    private fun createCleanUser(data: User): UserEntity {
        val user =
            UserEntity.new {
                firstName = data.firstName.value
                lastName = data.lastName.value
                email = data.email.value
                password = data.password.value
                phone = data.phone.normalize()
                cpf = data.cpf.normalize()
                isActive = true
                isEmailValid = false
                address = if (data.addressId > 0) AddressEntity[data.addressId] else null
                role = data.role
            }

        return user
    }

    override suspend fun getByEmail(email: Email): User? =
        suspendedQuery {
            UserEntity.find { UsersTable.email eq email.value }.firstOrNull()?.toModel()
        }

    override suspend fun getByCpf(cpf: CPF): User? =
        suspendedQuery {
            UserEntity.find { UsersTable.cpf eq cpf.normalize() }.firstOrNull()?.toModel()
        }

    override suspend fun getByCnpj(cnpf: CNPJ): User? =
        suspendedQuery {
            val company = CompanyEntity.find { CompanyTable.cnpj eq cnpf.normalize() }.firstOrNull()

            company?.user?.toModel()
        }
}
