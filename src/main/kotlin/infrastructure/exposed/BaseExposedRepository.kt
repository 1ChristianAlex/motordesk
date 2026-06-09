package com.khrix.infrastructure.exposed

import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

abstract class BaseExposedRepository<T : IntEntity, Model>(private val database: R2dbcDatabase) {
    suspend fun <T> suspendedQuery(block: suspend () -> T): T = suspendTransaction(database) {
        block()
    }
}
