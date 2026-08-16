package com.khrix.infrastructure.exposed.serviceorder.repository

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.repository.ServiceOrderApprovalRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderEntity
import org.jetbrains.exposed.v1.jdbc.Database

class ServiceOrderApprovalRepositoryImpl(
    database: Database,
) : BaseExposedRepository<ServiceOrderEntity, ServiceOrder>(database),
    ServiceOrderApprovalRepository {
    override suspend fun read(id: Int): ServiceOrder? {
        TODO("Not yet implemented")
    }

    override suspend fun update(
        id: Int,
        data: ServiceOrder,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun create(data: ServiceOrder): Int {
        TODO("Not yet implemented")
    }
}
