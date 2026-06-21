package testutils

import com.khrix.domain.company.model.Company
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.user.address.model.Address
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.model.User
import com.khrix.domain.vehicle.model.FuelType
import com.khrix.domain.vehicle.model.Vehicle
import com.khrix.domain.valueobject.Price
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email
import com.khrix.domain.valueobject.user.Name
import com.khrix.domain.valueobject.user.Password
import com.khrix.domain.valueobject.user.Phone
import com.khrix.domain.valueobject.user.CPF as CPFVO
import com.khrix.domain.valueobject.vehicle.Plate
import com.khrix.domain.valueobject.vehicle.Year
import com.khrix.domain.valueobject.company.CNPJ
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

// Helper functions to create valid domain objects used by tests

fun now(): LocalDateTime = LocalDateTime(2020, 1, 1, 0, 0)

fun sampleAddress(id: Int = 1): Address = Address(
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
    updatedAt = now()
)

fun sampleCompany(id: Int = 1): Company = Company(
    id = id,
    name = Name("Company"),
    cnpj = CNPJ("12345678000195"),
    createdAt = now(),
    updatedAt = now()
)

fun sampleUser(
    id: Int = 1,
    role: Role = Role.CLIENT,
    companyId: Int? = null,
    isActive: Boolean = true
): User = User(
    id = id,
    addressId = 1,
    companyId = companyId,
    firstName = Name("John"),
    lastName = Name("Doe"),
    email = Email("john.doe@example.com"),
    password = Password("Passw0rd!"),
    phone = Phone("+1234567890"),
    cpf = CPF("11144477735"),
    isActive = isActive,
    role = role,
    createdAt = now(),
    updatedAt = now()
)

fun sampleVehicle(ownerId: Int = 1, id: Int = 1): Vehicle = Vehicle(
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
    updatedAt = now()
)

fun sampleTask(id: Int = 1): Task = Task(
    id = id,
    name = "Task name",
    description = null,
    estimatedMinutes = 30,
    price = Price(BigDecimal("100.00")),
    isActive = true,
    category = com.khrix.domain.serviceorder.task.model.TaskCategory.GENERAL_REVIEW
)

fun sampleInventoryItem(id: Int = 1): InventoryItem = InventoryItem(
    id = id,
    sku = "SKU$id",
    name = "Item",
    description = null,
    quantity = 10,
    minimumQuantity = 1,
    unitPrice = Price(BigDecimal("10.00")),
    isActive = true
)

fun sampleServiceOrder(
    id: Int = 1,
    client: User = sampleUser(),
    operator: User = sampleUser(id = 2, role = Role.MANAGER),
    vehicle: Vehicle = sampleVehicle(),
    tasks: List<Task> = listOf(sampleTask()),
    inventoryItems: List<InventoryItem> = listOf()
): ServiceOrder = ServiceOrder(
    id = id,
    client = client,
    operator = operator,
    vehicle = vehicle,
    complaint = "Complaint about the vehicle",
    diagnosis = null,
    tasks = tasks,
    inventoryItems = inventoryItems,
    status = ServiceOrderStatus.CREATED
)

