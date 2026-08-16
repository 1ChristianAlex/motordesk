package com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers

import com.khrix.domain.serviceorder.task.model.Task
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.TaskOutputDto

fun Task.toOutputDto(): TaskOutputDto =
    TaskOutputDto(
        id = this.id,
        name = this.name,
        description = this.description,
        estimatedMinutes = this.estimatedMinutes,
        price = this.price.value,
        isActive = this.isActive,
        category = this.category,
        status = this.status,
    )
