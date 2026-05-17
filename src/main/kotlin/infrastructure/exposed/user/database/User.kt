package com.khrix.infrastructure.exposed.user.database


import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.address.database.AddressTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UsersTable : BaseTable("users") {
    val addressId = reference("addressId", AddressTable.id).nullable()

    val firstName = varchar("firstName", 100)
    val lastName = varchar("lastName", 100)

    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("passwordHash", 255)

    val phone = varchar("phone", 30)
    val cpf = varchar("cpf", 11)

    val isActive = bool("isActive").default(true)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var address by AddressEntity optionalReferencedOn UsersTable.addressId

    var firstName by UsersTable.firstName
    var lastName by UsersTable.lastName
    var email by UsersTable.email
    var password by UsersTable.password
    var phone by UsersTable.phone
    var cpf by UsersTable.cpf
    var isActive by UsersTable.isActive
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}