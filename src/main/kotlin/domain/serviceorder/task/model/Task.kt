package com.khrix.domain.serviceorder.task.model

import java.math.BigDecimal

data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val estimatedMinutes: Int,
    val price: BigDecimal,
    val isActive: Boolean
)