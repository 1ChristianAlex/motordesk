package com.khrix.infrastructure.exposed

import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

abstract class BaseExposedRepository<T : IntEntity, Model>(private val database: Database) {
    suspend fun <T> suspendedQuery(block: suspend () -> T): T = suspendTransaction(database) {
        block()
    }
}
