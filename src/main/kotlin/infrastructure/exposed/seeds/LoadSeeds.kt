package com.khrix.infrastructure.exposed.seeds

import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.company.database.CompanyEntity
import com.khrix.infrastructure.exposed.user.database.UserEntity
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class LoadSeeds {
    fun loadSeeds(database: Database) {
        transaction(database) {
            createDefaultUser()
            createCompanyUser()
        }
    }

    fun createCompanyUser() {
        val addressData = AddressEntity.new {
            street = "Rua das Flores"
            number = "123"
            complement = "Apto 2B"
            neighborhood = "Centro"
            city = "São Paulo"
            state = "SP"
            country = "Brazil"
            zipCode = "01310100"
        }
        val userData = UserEntity.new {
            firstName = "Christian"
            lastName = "Alexsander"
            email = "christian.alex@email.com"
            password = "test@123!"
            phone = "4737339296"
            cpf = "21641780096"
            isActive = true
            isEmailValid = true
            address = addressData
        }
        CompanyEntity.new {
            name = "Fake Company"
            cnpj = "22855604000152"
            user = userData
        }
    }

    fun createDefaultUser() {
        val addressData = AddressEntity.new {
            street = "Rua das Flores"
            number = "123"
            complement = "Apto 4B"
            neighborhood = "Centro"
            city = "São Paulo"
            state = "SP"
            country = "Brazil"
            zipCode = "01310100"
        }

        UserEntity.new {
            firstName = "Chris"
            lastName = "Alexsander"
            email = "christian.alexsander@email.com"
            password = "test@123!"
            phone = "4737339296"
            cpf = "21641780096"
            isActive = true
            isEmailValid = true
            address = addressData
        }
    }
}


