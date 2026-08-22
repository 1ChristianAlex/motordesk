package com.khrix.domain.serviceorder.task.port

import com.khrix.domain.history.port.DiffResolver
import com.khrix.domain.serviceorder.task.model.Task

interface TaskDiffResolver : DiffResolver<Task>
