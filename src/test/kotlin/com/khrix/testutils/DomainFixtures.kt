package com.khrix.testutils

import com.khrix.domain.company.model.Company
import com.khrix.domain.email.model.AddressEmailMetadata
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import com.khrix.domain.email.model.UserEmailMetadata
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.task.model.TaskCategory
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import com.khrix.domain.user.address.model.Address
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.Price
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.CompanyName
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.domain.vehicle.model.Vehicle
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

// Helper functions to create valid domain objects used by tests

fun now(): LocalDateTime = LocalDateTime(2020, 1, 1, 0, 0)

fun sampleAddress(id: Int = 1): Address =
    Address(
        id = id,
        street = "Street",
        number = "123",
        complement = null,
        neighborhood = "Neighborhood",
        city = "City",
        state = "ST",
        country = "Country",
        zipCode = "00000-000",
        createdAt = now(),
        updatedAt = now(),
    )

fun sampleCompany(id: Int = 1): Company =
    Company(
        id = id,
        name = CompanyName("Company"),
        cnpj = CNPJ("12345678000195"),
        createdAt = now(),
        updatedAt = now(),
        userId = 0,
    )

fun sampleUser(
    id: Int = 1,
    role: Role = Role.CLIENT,
    companyId: Int? = null,
    isActive: Boolean = true,
): User =
    User(
        id = id,
        addressId = 1,
        companyId = companyId,
        firstName = Name("John"),
        lastName = Name("Doe"),
        email = Email("john.doe@example.com"),
        password = Password.Raw("Passw0rd!"),
        phone = Phone("+1234567890"),
        cpf = CPF("11144477735"),
        isActive = isActive,
        role = role,
        createdAt = now(),
        updatedAt = now(),
    )

fun sampleVehicle(
    ownerId: Int = 1,
    id: Int = 1,
): Vehicle =
    Vehicle(
        id = id,
        ownerId = ownerId,
        plate = Plate("ABC1D23"),
        brand = "Brand",
        model = "Model",
        color = "Color",
        year = Year(now().year),
        mileage = 0,
        chassis = "CHASSIS123",
        fuelType = FuelType.GASOLINE,
        createdAt = now(),
        updatedAt = now(),
    )

fun sampleTask(id: Int = 1): Task =
    Task(
        id = id,
        name = "Task name",
        description = null,
        estimatedMinutes = 30,
        price = Price(BigDecimal("100.00")),
        isActive = true,
        category = com.khrix.domain.serviceorder.task.model.TaskCategory.GENERAL_REVIEW,
        status = TaskProgressStatus.NOT_STARTED,
    )

fun sampleInventoryItem(id: Int = 1): InventoryItem =
    InventoryItem(
        id = id,
        sku = "SKU$id",
        name = "Item",
        description = null,
        quantity = 10,
        minimumQuantity = 1,
        unitPrice = Price(BigDecimal("10.00")),
        isActive = true,
    )

fun sampleServiceOrder(
    id: Int = 1,
    client: User = sampleUser(),
    operator: User = sampleUser(id = 2, role = Role.MANAGER),
    vehicle: Vehicle = sampleVehicle(),
    tasks: List<Task> = listOf(sampleTask()),
    inventoryItems: List<InventoryItem> = listOf(),
): ServiceOrder =
    ServiceOrder(
        id = id,
        client = client,
        operator = operator,
        vehicle = vehicle,
        complaint = "Complaint about the vehicle",
        diagnosis = null,
        tasks = tasks,
        inventoryItems = inventoryItems,
        status = ServiceOrderStatus.CREATED,
    )

fun sampleEmailQueueItem() =
    EmailQueueItem(
        id = 1,
        orderCode = "#WT0jrd6ki5OymnsAFXpv",
        recipient = "christian.alexsander@email.com",
        subject = "Service Order Created",
        metadata =
            ServiceOrderEmailMetadata(
                client =
                    UserEmailMetadata(
                        id = 1,
                        address =
                            AddressEmailMetadata(
                                street = "Rua das Flores",
                                number = "123",
                                complement = "Apto 4B",
                                neighborhood = "Centro",
                                city = "São Paulo",
                                state = "SP",
                                zipCode = "01310100",
                            ),
                        firstName = "Chris",
                        lastName = "Alexsander",
                        email = "christian.alexsander@email.com",
                        phone = "4737339296",
                        cpf = "84783736081",
                        role = Role.CLIENT,
                    ),
                operator =
                    UserEmailMetadata(
                        id = 2,
                        address = null,
                        firstName = "Christian",
                        lastName = "Alexsander",
                        email = "christian.alex@email.com",
                        phone = "4737339296",
                        cpf = "21641780096",
                        role = Role.ADMIN,
                    ),
                vehicle =
                    Vehicle(
                        id = 1,
                        ownerId = 1,
                        plate = Plate("ABC1E23"),
                        brand = "Honda",
                        model = "Civic",
                        color = "Prata",
                        year = Year(2020),
                        mileage = 45_800,
                        chassis = "9BRBD48E0D1234567",
                        fuelType = FuelType.GASOLINE,
                        createdAt = LocalDateTime(2026, 8, 16, 11, 53, 37, 219_043_000),
                        updatedAt = LocalDateTime(2026, 8, 16, 11, 53, 37, 219_043_000),
                    ),
                status = ServiceOrderStatus.CREATED,
                complaint = "Cliente chegou no momento de revisão por quilometragem",
                diagnosis = "Revisão e balanceamento",
                tasks =
                    listOf(
                        Task(
                            id = 1,
                            name = "Troca de Óleo",
                            description = "Substituição do óleo do motor e verificação dos níveis",
                            estimatedMinutes = 30,
                            price = Price(BigDecimal("89.90")),
                            isActive = true,
                            category = TaskCategory.ELECTRICAL,
                            status = TaskProgressStatus.NOT_STARTED,
                        ),
                        Task(
                            id = 2,
                            name = "Troca de Filtro de Óleo",
                            description = "Substituição do filtro de óleo do motor",
                            estimatedMinutes = 15,
                            price = Price(BigDecimal("39.90")),
                            isActive = true,
                            category = TaskCategory.ELECTRICAL,
                            status = TaskProgressStatus.NOT_STARTED,
                        ),
                        Task(
                            id = 9,
                            name = "Troca de Amortecedores",
                            description = "Substituição dos amortecedores do veículo",
                            estimatedMinutes = 120,
                            price = Price(BigDecimal("299.90")),
                            isActive = true,
                            category = TaskCategory.ELECTRICAL,
                            status = TaskProgressStatus.NOT_STARTED,
                        ),
                    ),
                inventoryItems =
                    listOf(
                        InventoryItem(
                            id = 2,
                            sku = "FILTRO-OLEO-001",
                            name = "Filtro de Óleo",
                            description = "Filtro de óleo para veículos de passeio",
                            quantity = 30,
                            minimumQuantity = 5,
                            unitPrice = Price(BigDecimal("24.90")),
                            isActive = true,
                        ),
                        InventoryItem(
                            id = 5,
                            sku = "VELA-IGNICAO-001",
                            name = "Vela de Ignição",
                            description = "Vela para motores flex",
                            quantity = 100,
                            minimumQuantity = 20,
                            unitPrice = Price(BigDecimal("18.50")),
                            isActive = true,
                        ),
                        InventoryItem(
                            id = 6,
                            sku = "PASTILHA-FREIO-001",
                            name = "Pastilha de Freio Dianteira",
                            description = "Jogo de pastilhas de freio",
                            quantity = 15,
                            minimumQuantity = 3,
                            unitPrice = Price(BigDecimal("129.90")),
                            isActive = true,
                        ),
                    ),
                totalAmount = BigDecimal("4975.20"),
            ),
        status = EmailStatus.PENDING,
        attempts = 0,
        errorMessage = null,
    )
