package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.exposed.address.database.AddressTable
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

abstract class DatabaseConnection {
    abstract val database: Database

    companion object {
        fun getConnection(database: Database): Database {
            return database.apply {
                transaction {
                    SchemaUtils.create(
                        UsersTable,
                        AddressTable,
                    )
                }
            }
        }
    }
}