package com.khrix.infrastructure.exposed.serviceorder.repository

import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken
import com.khrix.domain.serviceorder.repository.ServiceOrderApprovalRepository
import com.khrix.domain.serviceorder.repository.ServiceOrderRepository
import com.khrix.infrastructure.exposed.BaseExposedRepository
import com.khrix.infrastructure.exposed.serviceorder.database.OrderApprovalEntity
import com.khrix.infrastructure.exposed.serviceorder.database.OrderApprovalTable
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.infrastructure.exposed.serviceorder.mapper.toModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

class ServiceOrderApprovalExposedRepositoryImpl(
    database: Database,
    private val serviceOrderRepository: ServiceOrderRepository,
) : BaseExposedRepository<ServiceOrderEntity, ServiceOrderApprovalToken>(database),
    ServiceOrderApprovalRepository {
    private suspend fun getServiceOrder(serviceOrderCode: String): ServiceOrderEntity {
        val serviceOrder = serviceOrderRepository.getByCode(serviceOrderCode)!!.id

        return suspendedQuery {
            ServiceOrderEntity[serviceOrder]
        }
    }

    override suspend fun read(id: Int): ServiceOrderApprovalToken? =
        suspendedQuery {
            OrderApprovalEntity.findById(id)?.toModel()
        }

    override suspend fun update(
        id: Int,
        data: ServiceOrderApprovalToken,
    ) {
        val serviceOrderEntity = getServiceOrder(data.serviceOrderCode)
        suspendedQuery {
            OrderApprovalEntity.findByIdAndUpdate(id) {
                it.serviceOrder = serviceOrderEntity
                it.tokenHash = data.tokenHash
                it.expiresAt = data.expiresAt
                it.usedAt = data.usedAt
            }
        }
    }

    override suspend fun createRead(data: ServiceOrderApprovalToken): ServiceOrderApprovalToken {
        val serviceOrderEntity = getServiceOrder(data.serviceOrderCode)
        return suspendedQuery {
            OrderApprovalEntity
                .new {
                    serviceOrder = serviceOrderEntity
                    tokenHash = data.tokenHash
                    expiresAt = data.expiresAt
                    usedAt = data.usedAt
                }.toModel()
        }
    }

    override suspend fun getByToken(token: String): ServiceOrderApprovalToken? =
        suspendedQuery {
            OrderApprovalEntity.find { OrderApprovalTable.tokenHash eq token }.map { it.toModel() }.firstOrNull()
        }
}
