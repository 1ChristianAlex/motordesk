package com.khrix.adapter.outbound.exposed.connections

import com.khrix.adapter.outbound.exposed.DatabaseSchemas
import com.khrix.adapter.outbound.exposed.address.database.AddressTable
import com.khrix.adapter.outbound.exposed.company.database.CompanyTable
import com.khrix.adapter.outbound.exposed.email.database.EmailQueueTable
import com.khrix.adapter.outbound.exposed.inventory.database.InventoryTable
import com.khrix.adapter.outbound.exposed.seeds.LoadSeeds
import com.khrix.adapter.outbound.exposed.serviceorder.database.OrderApprovalTable
import com.khrix.adapter.outbound.exposed.serviceorder.database.ServiceOrderPartsTable
import com.khrix.adapter.outbound.exposed.serviceorder.database.ServiceOrderTasksTable
import com.khrix.adapter.outbound.exposed.serviceorder.database.ServiceOrdersTable
import com.khrix.adapter.outbound.exposed.serviceorder.database.TaskTable
import com.khrix.adapter.outbound.exposed.user.database.UsersTable
import com.khrix.adapter.outbound.exposed.vehicles.database.VehicleTable
import org.jetbrains.exposed.v1.core.Schema
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory

abstract class DatabaseConnection(
    private val loadSeeds: LoadSeeds,
) {
    abstract val database: Database
    private val logger = LoggerFactory.getLogger(javaClass)

    private val schemaList =
        DatabaseSchemas.entries
            .map {
                Schema(it.value)
            }.toTypedArray()

    private val tableList =
        listOf(
            UsersTable,
            AddressTable,
            CompanyTable,
            VehicleTable,
            InventoryTable,
            TaskTable,
            ServiceOrdersTable,
            ServiceOrderPartsTable,
            ServiceOrderTasksTable,
            OrderApprovalTable,
            EmailQueueTable,
        ).toTypedArray()

    fun JdbcTransaction.beforeLoad() {
        addLogger(StdOutSqlLogger)
        SchemaUtils.drop(
            *tableList,
        )
    }

    fun JdbcTransaction.afterLoad() {
        loadSeeds.loadSeeds(this.db)
    }

    fun getConnection(): Database =
        try {
            logger.info("Starting exposed database")
            database.apply {
                transaction {
                    beforeLoad()
                    SchemaUtils.createSchema(
                        *schemaList,
                    )
                    SchemaUtils.create(
                        *tableList,
                    )
                    afterLoad()
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to establish database connection: ${e.message}", e)
        }
}
