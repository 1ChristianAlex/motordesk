package com.khrix.adapter.exposed.user.database

import com.khrix.adapter.exposed.BaseTable
import com.khrix.adapter.exposed.address.database.AddressEntity
import com.khrix.adapter.exposed.address.database.AddressTable
import com.khrix.adapter.exposed.company.database.CompanyEntity
import com.khrix.adapter.exposed.company.database.CompanyTable
import com.khrix.domain.user.model.Role
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UsersTable : BaseTable("users") {
    val address = reference("addressId", AddressTable).nullable()
    val role = enumerationByName<Role>("role", 20).default(Role.CLIENT)

    val firstName = varchar("firstName", 100)
    val lastName = varchar("lastName", 100)

    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("passwordHash", 255)

    val phone = varchar("phone", 30)
    val cpf = varchar("cpf", 11).uniqueIndex()

    val isActive = bool("isActive").default(true)
    val isEmailValid = bool("isEmailValid").default(false)
}

class UserEntity(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var address by AddressEntity optionalReferencedOn UsersTable.address
    val company by CompanyEntity optionalBackReferencedOn CompanyTable.user

    var firstName by UsersTable.firstName
    var lastName by UsersTable.lastName
    var email by UsersTable.email
    var password by UsersTable.password
    var phone by UsersTable.phone
    var cpf by UsersTable.cpf
    var isActive by UsersTable.isActive
    var isEmailValid by UsersTable.isEmailValid
    var role by UsersTable.role

    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}
