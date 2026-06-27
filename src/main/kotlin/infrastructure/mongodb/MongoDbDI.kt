package com.khrix.infrastructure.mongodb

import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderTaskHistoryRepository
import com.khrix.infrastructure.mongodb.connection.MongoConnection
import com.khrix.infrastructure.mongodb.serviceorder.repository.ServiceOrderMongoRepositoryImpl
import com.khrix.infrastructure.mongodb.serviceorder.repository.ServiceOrderTaskMongoRepositoryImpl
import io.ktor.server.plugins.di.*

fun appMongoDb(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide(MongoConnection::class)
        provide<ServiceOrderHistoryRepository>(ServiceOrderMongoRepositoryImpl::class)
        provide<ServiceOrderTaskHistoryRepository>(ServiceOrderTaskMongoRepositoryImpl::class)
    }
}
