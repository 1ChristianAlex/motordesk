package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.exposed.address.database.AddressTable
import com.khrix.infrastructure.exposed.company.database.CompanyTable
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import com.khrix.infrastructure.exposed.user.database.UsersTable
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

abstract class DatabaseConnection(private val isDevelopment: Boolean, private val loadSeeds: LoadSeeds) {
    abstract val database: Database

    fun JdbcTransaction.beforeLoad() {
        if (isDevelopment) {
            addLogger(StdOutSqlLogger)
        }
    }

    fun JdbcTransaction.afterLoad() {
        if (isDevelopment) {
            loadSeeds.loadSeeds(this.db)
        }
    }


    fun getConnection(): Database {
        return database.apply {
            transaction {
                val tableList = listOf(
                    UsersTable,
                    AddressTable,
                    CompanyTable,
                ).toTypedArray()

                beforeLoad()

                SchemaUtils.create(
                    *tableList
                )

                afterLoad()
            }
        }
    }
}