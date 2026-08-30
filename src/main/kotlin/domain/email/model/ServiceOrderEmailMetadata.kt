package com.khrix.domain.email.model

import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.user.address.model.Address
import com.khrix.domain.user.model.Role
import com.khrix.domain.vehicle.model.Vehicle
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class UserEmailMetadata(
    val id: Int,
    val address: AddressEmailMetadata?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val role: Role,
)

@Serializable
data class AddressEmailMetadata(
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String
)

@Serializable
data class ServiceOrderEmailMetadata(
    val client: UserEmailMetadata,
    val operator: UserEmailMetadata,
    val vehicle: Vehicle,
    val status: ServiceOrderStatus,
    val complaint: String,
    val diagnosis: String? = null,
    val tasks: List<Task>,
    val inventoryItems: List<InventoryItem> = listOf(),
    @Serializable(with = DecimalAsStringSerializer::class)
    val totalAmount: BigDecimal
) {
    constructor(serviceOrder: ServiceOrder, clientAddress: Address) : this(
        client = UserEmailMetadata(
            id = serviceOrder.client.id,
            address = AddressEmailMetadata(
                street = clientAddress.street,
                number = clientAddress.number,
                complement = clientAddress.complement,
                neighborhood = clientAddress.neighborhood,
                city = clientAddress.city,
                state = clientAddress.state,
                zipCode = clientAddress.zipCode
            ),
            firstName = serviceOrder.client.firstName.value,
            lastName = serviceOrder.client.lastName.value,
            email = serviceOrder.client.email.value,
            phone = serviceOrder.client.phone.value,
            cpf = serviceOrder.client.cpf.value,
            role = serviceOrder.client.role
        ),
        operator = UserEmailMetadata(
            id = serviceOrder.operator.id,
            address = null,
            firstName = serviceOrder.operator.firstName.value,
            lastName = serviceOrder.operator.lastName.value,
            email = serviceOrder.operator.email.value,
            phone = serviceOrder.operator.phone.value,
            cpf = serviceOrder.operator.cpf.value,
            role = serviceOrder.operator.role
        ),
        vehicle = serviceOrder.vehicle,
        status = serviceOrder.status,
        complaint = serviceOrder.complaint,
        diagnosis = serviceOrder.diagnosis,
        tasks = serviceOrder.tasks.toList(),
        inventoryItems = serviceOrder.inventoryItems.toList(),
        totalAmount = serviceOrder.totalPrice
    )
}
