package com.khrix.infrastructure.http.controllers.serviceorder.resources.dto

import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import com.khrix.domain.serviceorder.task.model.TaskCategory
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class TaskOutputDto(
    val id: Int,
    val name: String,
    val description: String?,
    val estimatedMinutes: Int,
    @Serializable(with = DecimalAsStringSerializer::class)
    val price: BigDecimal,
    val isActive: Boolean,
    val category: TaskCategory,
    val status: TaskProgressStatus,
)
