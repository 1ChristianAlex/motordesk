package com.khrix.domain.serviceorder.task.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.serviceorder.task.model.Task

interface GetTaskByListIdUseCase : BaseUseCase<List<Int>, List<Task>>
