package com.khrix.infrastructure.exposed.company.database

import com.khrix.infrastructure.exposed.BaseTable
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object CompanyTable : BaseTable("company") {
    val name = varchar("name", 255)
    val cnpj = varchar("cnpj", 14).uniqueIndex()
    val user = reference("userId", UsersTable).uniqueIndex()
}

class CompanyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompanyEntity>(CompanyTable)

    var name by CompanyTable.name
    var cnpj by CompanyTable.cnpj

    var createdAt by CompanyTable.createdAt
    var updatedAt by CompanyTable.updatedAt

    var user by UserEntity referencedOn CompanyTable.user
}
