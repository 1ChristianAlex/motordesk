package com.khrix.domain.serviceorder.model

import com.khrix.domain.inventory.model.InventoryItem
import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.DomainValidation
import com.khrix.domain.vehicle.model.Vehicle
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank
import java.math.BigDecimal

data class ServiceOrder(
    val client: User,
    val operator: User,
    val vehicle: Vehicle,
    val status: ServiceOrderStatus,
    val complaint: String,
    val diagnosis: String? = null,
    val tasks: List<Task>,
    val inventoryItems: List<InventoryItem> = listOf()
) : DomainValidation<ServiceOrder>() {
    override val validation = Validation.Companion<ServiceOrder> {
        ServiceOrder::client  {
            constrain("Client must have CLIENT role") { it.role == Role.CLIENT }
            constrain("Client must be active") { it.isActive }
        }

        ServiceOrder::operator  {
            constrain("Operator must not have CLIENT role") { it.role != Role.CLIENT }
            constrain("Operator must be active") { it.isActive }
        }

        ServiceOrder::vehicle  {
            constrain("Vehicle must belong to the client") { it.ownerId == client.id }
        }

        ServiceOrder::complaint  {
            notBlank() hint "Complaint cannot be empty"
            constrain("Complaint must be at least 10 characters") { it.length >= 10 }
            constrain("Complaint must be at most 1000 characters") { it.length <= 1000 }
        }

        ServiceOrder::tasks  {
            constrain("Tasks cannot be empty") { it.isNotEmpty() }
            constrain("Tasks must be active") { it.all { task -> task.isActive } }
        }

        ServiceOrder::inventoryItems  {
            constrain("Inventory items must be active") { it.all { inventoryItem -> inventoryItem.isActive } }
        }
    }

    val totalAmount: BigDecimal
        get() {
            return calculateTotal()
        }

    private fun calculateTotal(): BigDecimal = BigDecimal.ZERO
        .add(tasks.fold(BigDecimal.ZERO) { acc, task ->
            acc.add(task.price.value)
        })
        .add(inventoryItems.fold(BigDecimal.ZERO) { acc, item ->
            acc.add(item.unitPrice.value * item.quantity.toBigDecimal())
        })
}