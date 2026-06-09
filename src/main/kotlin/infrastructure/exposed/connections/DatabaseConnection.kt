package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.exposed.address.database.AddressTable
import com.khrix.infrastructure.exposed.company.database.CompanyTable
import com.khrix.infrastructure.exposed.email.database.EmailQueueTable
import com.khrix.infrastructure.exposed.inventory.database.InventoryTable
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderPartsTable
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderTasksTable
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrdersTable
import com.khrix.infrastructure.exposed.serviceorder.database.TaskTable
import com.khrix.infrastructure.exposed.user.database.UsersTable
import com.khrix.infrastructure.exposed.vehicles.database.VehicleTable
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

abstract class DatabaseConnection(private val isDevelopment: Boolean, private val loadSeeds: LoadSeeds) {
    abstract val database: R2dbcDatabase

    private val tableList = listOf(
        UsersTable,
        AddressTable,
        CompanyTable,
        VehicleTable,
        InventoryTable,
        TaskTable,
        ServiceOrdersTable,
        ServiceOrderPartsTable,
        ServiceOrderTasksTable,
        EmailQueueTable
    ).toTypedArray()

    fun JdbcTransaction.beforeLoad() {
        if (isDevelopment) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(
                *tableList
            )
        }
    }

    fun JdbcTransaction.afterLoad() {
        if (isDevelopment) {
            loadSeeds.loadSeeds(this.db)
        }
    }


    fun getConnection(): R2dbcDatabase {
        return database.apply {
            transaction {
                beforeLoad()
                SchemaUtils.create(
                    *tableList
                )
                afterLoad()
            }
        }
    }
}