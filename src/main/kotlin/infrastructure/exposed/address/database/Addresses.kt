package com.khrix.infrastructure.exposed.address.database

import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object AddressTable : BaseTable("address") {
    val street = varchar("street", 255)
    val number = varchar("number", 20)

    val complement = varchar("complement", 100).nullable()
    val neighborhood = varchar("neighborhood", 100)

    val city = varchar("city", 100)
    val state = varchar("state", 100)
    val country = varchar("country", 100)

    val zipCode = varchar("zipCode", 20)
}

class AddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AddressEntity>(AddressTable)

    var street by AddressTable.street
    var number by AddressTable.number
    var complement by AddressTable.complement
    var neighborhood by AddressTable.neighborhood
    var city by AddressTable.city
    var state by AddressTable.state
    var country by AddressTable.country
    var zipCode by AddressTable.zipCode

    var createdAt by AddressTable.createdAt
    var updatedAt by AddressTable.updatedAt

    val user by UserEntity optionalReferencedOn UsersTable.addressId
}