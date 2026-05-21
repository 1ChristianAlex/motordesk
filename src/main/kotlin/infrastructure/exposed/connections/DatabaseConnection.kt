package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.exposed.address.database.AddressTable
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

abstract class DatabaseConnection(private val isDevelopment: Boolean) {
    abstract val database: Database

    fun getConnection(): Database {

        return database.apply {
            transaction {
                val tableList = listOf(
                    UsersTable,
                    AddressTable,
                ).toTypedArray()
                if (isDevelopment) {
                    addLogger(StdOutSqlLogger)
                }

                SchemaUtils.create(
                    *tableList
                )
            }
        }
    }
}