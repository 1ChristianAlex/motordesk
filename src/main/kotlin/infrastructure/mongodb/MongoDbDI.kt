package com.khrix.infrastructure.mongodb

import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderTaskHistoryRepository
import com.khrix.infrastructure.mongodb.connection.MongoConnection
import com.khrix.infrastructure.mongodb.serviceorder.repository.ServiceOrderMongoRepositoryImpl
import com.khrix.infrastructure.mongodb.serviceorder.repository.ServiceOrderTaskMongoRepositoryImpl
import io.ktor.events.Events
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.DependencyRegistry

fun appMongoDb(
    dependencies: DependencyRegistry,
    monitor: Events,
) {
    with(dependencies) {
        provide(MongoConnection::class)
        provide<ServiceOrderHistoryRepository>(ServiceOrderMongoRepositoryImpl::class)
        provide<ServiceOrderTaskHistoryRepository>(ServiceOrderTaskMongoRepositoryImpl::class)
        monitor.subscribe(ApplicationStopping) {
            val mongoConnection: MongoConnection by dependencies
            mongoConnection.close()
        }
    }
}
