package com.khrix.infrastructure.mongodb

import com.khrix.domain.serviceorder.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.task.repository.TaskHistoryRepository
import com.khrix.infrastructure.mongodb.connection.MongoConnection
import com.khrix.infrastructure.mongodb.registerHistory.database.RegisterHistoryDocument
import com.khrix.infrastructure.mongodb.registerHistory.repository.RegisterHistoryRepositoryMongoFactory
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
