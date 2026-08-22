package com.khrix.adapter.outbound.mongodb

import com.khrix.adapter.outbound.mongodb.connection.MongoConnection
import com.khrix.adapter.outbound.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.adapter.outbound.mongodb.registerHistory.repository.RegisterHistoryRepositoryMongoFactory
import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.task.repository.TaskHistoryRepository
import io.ktor.events.Events
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.plugins.di.DependencyRegistry

fun appMongoDb(
    dependencies: DependencyRegistry,
    monitor: Events,
) {
    with(dependencies) {
        provide(MongoConnection::class)
        provide<ServiceOrderHistoryRepository> {
            val mongoConnection = resolve<MongoConnection>()
            object :
                RegisterHistoryRepositoryMongoFactory(mongoConnection, RegisterHistoryDocument.SERVICE_ORDER_HISTORY),
                ServiceOrderHistoryRepository {
            }
        }
        provide<TaskHistoryRepository> {
            val mongoConnection = resolve<MongoConnection>()
            object :
                RegisterHistoryRepositoryMongoFactory(
                    mongoConnection,
                    RegisterHistoryDocument.SERVICE_ORDER_TASK_HISTORY,
                ),
                TaskHistoryRepository {
            }
        }

        monitor.subscribe(ApplicationStopping) {
            val mongoConnection: MongoConnection by dependencies
            mongoConnection.close()
        }
    }
}
