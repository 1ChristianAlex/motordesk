package com.khrix.domain.serviceorder.task.model

import com.khrix.domain.core.validateWith
import com.khrix.domain.valueobject.Price
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val estimatedMinutes: Int,
    val price: Price,
    val isActive: Boolean,
    val category: TaskCategory
) {
    private val validation = Validation.Companion<Task> {
        Task::name  {
            notBlank() hint "Task name cannot be empty"
            constrain("Task name must be at least 3 characters") { it.length >= 3 }
            constrain("Task name must be at most 150 characters") { it.length <= 150 }
        }
        Task::estimatedMinutes {
            constrain("Estimated minutes must be a positive integer") { it > 0 }
            constrain("Estimated minutes must be a multiple of 15") { it % 15 == 0 }
        }
    }

    init {
        validateWith(validation)
    }

    fun changePrice(newPrice: Price): Task {
        return this.copy(price = newPrice)
    }

    fun changeEstimatedMinutes(newEstimatedMinutes: Int): Task {
        return this.copy(estimatedMinutes = newEstimatedMinutes)
    }
}