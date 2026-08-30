package com.khrix.domain.serviceorder.task.model

enum class TaskCategory {
    ENGINE,
    SUSPENSION,
    BRAKES,
    ELECTRICAL,
    ALIGNMENT,
    AIR_CONDITIONING,
    DIAGNOSTIC,
    GENERAL_REVIEW,
}

enum class TaskProgressStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETE,
}
