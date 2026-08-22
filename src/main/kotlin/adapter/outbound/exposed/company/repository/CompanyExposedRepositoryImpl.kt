package com.khrix.adapter.outbound.exposed.company.repository

import com.khrix.adapter.outbound.exposed.BaseExposedRepository
import com.khrix.adapter.outbound.exposed.company.database.CompanyEntity
import com.khrix.adapter.outbound.exposed.company.database.CompanyTable
import com.khrix.adapter.outbound.exposed.company.mapper.toModel
import com.khrix.adapter.outbound.exposed.user.database.UserEntity
import com.khrix.domain.company.model.Company
import com.khrix.domain.company.repository.CompanyRepository
import com.khrix.domain.valueobject.company.CNPJ
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

class CompanyExposedRepositoryImpl(
    database: Database,
) : BaseExposedRepository<CompanyEntity, Company>(database),
    CompanyRepository {
    override suspend fun createRead(data: Company): Company =
        suspendedQuery {
            createNewCompany(data).toModel()
        }

    override suspend fun read(id: Int): Company =
        suspendedQuery {
            CompanyEntity[id].toModel()
        }

    override suspend fun update(
        id: Int,
        data: Company,
    ) {
        suspendedQuery {
            CompanyEntity.findByIdAndUpdate(id) {
                it.name = data.name.value
                it.cnpj = data.cnpj.normalize()
            }
        }
    }

    override suspend fun delete(id: Int) {
        suspendedQuery { CompanyEntity[id].delete() }
    }

    override suspend fun create(data: Company): Int =
        suspendedQuery {
            createNewCompany(data).id.value
        }

    override suspend fun findByCnpj(cnpj: CNPJ): Company? =
        suspendedQuery {
            CompanyEntity.find { CompanyTable.cnpj eq cnpj.normalize() }.firstOrNull()?.toModel()
        }

    private fun createNewCompany(data: Company) =
        CompanyEntity.new {
            name = data.name.value
            cnpj = data.cnpj.normalize()
            user = UserEntity[data.userId]
        }
}
