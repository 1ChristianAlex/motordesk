package com.khrix.infrastructure.exposed.serviceorder.mapper

import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.domain.valueobject.Price
import com.khrix.infrastructure.exposed.serviceorder.database.TaskEntity

fun TaskEntity.toModel(): Task = Task(
    id = id.value,
    name = name,
    description = description,
    estimatedMinutes = estimatedMinutes,
    price = Price(price),
    isActive = isActive,
    category = category,
)

