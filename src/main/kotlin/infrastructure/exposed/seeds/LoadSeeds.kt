package com.khrix.infrastructure.exposed.seeds

import com.khrix.domain.serviceorder.task.model.TaskCategory
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.infrastructure.exposed.address.database.AddressEntity
import com.khrix.infrastructure.exposed.company.database.CompanyEntity
import com.khrix.infrastructure.exposed.inventory.database.InventoryEntity
import com.khrix.infrastructure.exposed.inventory.database.InventoryTable
import com.khrix.infrastructure.exposed.serviceorder.database.ServiceOrderEntity
import com.khrix.infrastructure.exposed.serviceorder.database.TaskEntity
import com.khrix.infrastructure.exposed.serviceorder.database.TaskTable
import com.khrix.infrastructure.exposed.user.database.UserEntity
import com.khrix.infrastructure.exposed.user.database.UsersTable
import com.khrix.infrastructure.exposed.vehicles.database.VehicleEntity
import com.khrix.infrastructure.exposed.vehicles.database.VehicleTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.math.BigDecimal

class LoadSeeds(
    private val passwordHasher: PasswordHasher
) {
    fun loadSeeds(database: Database) {
        transaction(database) {
            createDefaultUser()
            createCompanyUser()
            createVehicle()
            createInventory()
            createServiceList()
            createServiceOrder()
        }
    }

    private fun createVehicle() {
        VehicleEntity.new {
            plate = "ABC1E23"
            model = "Civic"
            brand = "Honda"
            year = 2020
            color = "Prata"
            mileage = 45800
            owner = UserEntity.find { UsersTable.cpf eq "84783736081" }.first()
            fuelType = FuelType.GASOLINE
            chassis = "9BRBD48E0D1234567"
        }
    }

    private fun createServiceOrder() {
        val tasksList = TaskEntity.find { TaskTable.isActive eq true }.limit(2)
        val partsList = InventoryEntity.find { InventoryTable.isActive eq true }.limit(3)
        val vehicleItem = VehicleEntity.find { VehicleTable.plate eq "ABC1E23" }.first()
        val clientItem = UserEntity.find { UsersTable.cpf eq "84783736081" }.first()
        val operatorItem = UserEntity.find { UsersTable.cpf eq "21641780096" }.first()

        ServiceOrderEntity.new {
            client = clientItem
            operator = operatorItem
            tasks = tasksList
            parts = partsList
            vehicle = vehicleItem
            complaint = "This is a test"
            diagnosis = "There is a problem for sure"
            totalAmount = BigDecimal("10")
        }
    }

    private fun createServiceList() {
        TaskEntity.new {
            name = "Troca de Óleo"
            description = "Substituição do óleo do motor e verificação dos níveis"
            estimatedMinutes = 30
            price = BigDecimal("89.90")
            category = TaskCategory.ELECTRICAL
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Filtro de Óleo"
            description = "Substituição do filtro de óleo do motor"
            estimatedMinutes = 15
            price = BigDecimal("39.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Filtro de Ar"
            description = "Substituição do filtro de ar do motor"
            estimatedMinutes = 15
            price = BigDecimal("29.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Filtro de Cabine"
            description = "Substituição do filtro do ar-condicionado"
            estimatedMinutes = 15
            price = BigDecimal("34.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Alinhamento"
            description = "Alinhamento da direção e suspensão"
            estimatedMinutes = 60
            price = BigDecimal("79.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Balanceamento"
            description = "Balanceamento das rodas"
            estimatedMinutes = 30
            price = BigDecimal("59.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Pastilhas de Freio"
            description = "Substituição das pastilhas de freio dianteiras ou traseiras"
            estimatedMinutes = 60
            price = BigDecimal("149.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Discos de Freio"
            description = "Substituição dos discos de freio"
            estimatedMinutes = 90
            price = BigDecimal("199.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Amortecedores"
            description = "Substituição dos amortecedores do veículo"
            estimatedMinutes = 120
            price = BigDecimal("299.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Bateria"
            description = "Substituição da bateria automotiva"
            estimatedMinutes = 30
            price = BigDecimal("49.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Correia Dentada"
            description = "Substituição da correia dentada e inspeção do sistema"
            estimatedMinutes = 180
            price = BigDecimal("499.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Diagnóstico Eletrônico"
            description = "Leitura de falhas utilizando scanner automotivo"
            estimatedMinutes = 45
            price = BigDecimal("99.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Revisão Preventiva"
            description = "Inspeção geral dos principais componentes do veículo"
            estimatedMinutes = 180
            price = BigDecimal("399.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Higienização de Ar-Condicionado"
            description = "Limpeza e higienização completa do sistema de ar-condicionado"
            estimatedMinutes = 60
            price = BigDecimal("129.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Fluido de Freio"
            description = "Substituição completa do fluido do sistema de freios"
            estimatedMinutes = 45
            price = BigDecimal("89.90")
        }
        TaskEntity.new {
            category = TaskCategory.ELECTRICAL
            name = "Troca de Aditivo do Radiador"
            description = "Limpeza e troca do fluido de arrefecimento"
            estimatedMinutes = 60
            price = BigDecimal("119.90")
        }
    }

    private fun createInventory() {
        InventoryEntity.new {
            sku = "OLEO-5W30-001"
            name = "Óleo Motor 5W30"
            description = "Lubrificante sintético para motores flex"
            quantity = 50
            minimumQuantity = 10
            unitPrice = BigDecimal("49.90")
        }
        InventoryEntity.new {
            sku = "FILTRO-OLEO-001"
            name = "Filtro de Óleo"
            description = "Filtro de óleo para veículos de passeio"
            quantity = 30
            minimumQuantity = 5
            unitPrice = BigDecimal("24.90")
        }
        InventoryEntity.new {
            sku = "FILTRO-AR-001"
            name = "Filtro de Ar"
            description = "Filtro de ar do motor"
            quantity = 25
            minimumQuantity = 5
            unitPrice = BigDecimal("39.90")
        }
        InventoryEntity.new {
            sku = "FILTRO-CABINE-001"
            name = "Filtro de Cabine"
            description = "Filtro do ar-condicionado"
            quantity = 20
            minimumQuantity = 5
            unitPrice = BigDecimal("34.90")
        }
        InventoryEntity.new {
            sku = "VELA-IGNICAO-001"
            name = "Vela de Ignição"
            description = "Vela para motores flex"
            quantity = 100
            minimumQuantity = 20
            unitPrice = BigDecimal("18.50")
        }
        InventoryEntity.new {
            sku = "PASTILHA-FREIO-001"
            name = "Pastilha de Freio Dianteira"
            description = "Jogo de pastilhas de freio"
            quantity = 15
            minimumQuantity = 3
            unitPrice = BigDecimal("129.90")
        }
        InventoryEntity.new {
            sku = "DISCO-FREIO-001"
            name = "Disco de Freio Dianteiro"
            description = "Disco ventilado"
            quantity = 10
            minimumQuantity = 2
            unitPrice = BigDecimal("189.90")
        }
        InventoryEntity.new {
            sku = "AMORTECEDOR-001"
            name = "Amortecedor Dianteiro"
            description = "Amortecedor hidráulico"
            quantity = 8
            minimumQuantity = 2
            unitPrice = BigDecimal("349.90")
        }
        InventoryEntity.new {
            sku = "BATERIA-60AH-001"
            name = "Bateria 60Ah"
            description = "Bateria automotiva"
            quantity = 12
            minimumQuantity = 2
            unitPrice = BigDecimal("459.90")
        }
        InventoryEntity.new {
            sku = "CORREIA-DENTADA-001"
            name = "Correia Dentada"
            description = "Correia dentada para motores flex"
            quantity = 10
            minimumQuantity = 2
            unitPrice = BigDecimal("159.90")
        }
        InventoryEntity.new {
            sku = "LIQUIDO-FREIO-001"
            name = "Fluido de Freio DOT 4"
            description = "Fluido para sistema de freios"
            quantity = 40
            minimumQuantity = 10
            unitPrice = BigDecimal("29.90")
        }
        InventoryEntity.new {
            sku = "ADITIVO-RADIADOR-001"
            name = "Aditivo para Radiador"
            description = "Aditivo para sistema de arrefecimento"
            quantity = 20
            minimumQuantity = 5
            unitPrice = BigDecimal("44.90")
        }
    }

    private fun createCompanyUser() {
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
            password = passwordHasher.hash("test@123!")
            phone = "4737339296"
            cpf = "21641780096"
            isActive = true
            isEmailValid = true
            address = addressData
            role = Role.ADMIN
        }
        CompanyEntity.new {
            name = "Fake Company"
            cnpj = "22855604000152"
            user = userData
        }
    }

    private fun createDefaultUser() {
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
            password = passwordHasher.hash("test@123!")
            phone = "4737339296"
            cpf = "84783736081"
            isActive = true
            isEmailValid = true
            address = addressData
        }
    }
}


