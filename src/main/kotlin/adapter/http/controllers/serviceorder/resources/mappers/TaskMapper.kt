package com.khrix.adapter.http.controllers.serviceorder.resources.mappers

import com.khrix.adapter.http.controllers.serviceorder.resources.dto.TaskOutputDto
import com.khrix.domain.serviceorder.task.model.Task

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
